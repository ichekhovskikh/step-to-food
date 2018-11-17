package com.sugar.steptofood.db

import com.sugar.steptofood.model.*
import com.sugar.steptofood.db.dao.*
import com.sugar.steptofood.utils.RecipeType

class RecipeBusinessObject(private val recipeDao: RecipeDao,
                           private val userDao: UserDao,
                           private val productDao: ProductDao,
                           private val productRecipeDao: ProductRecipeDao,
                           private val userRecipeDao: UserRecipeDao) {

    fun removeAll() {
        userRecipeDao.removeAll()
        userDao.removeAll()
        productRecipeDao.removeAll()
        productDao.removeAll()
        recipeDao.removeAll()
    }

    fun getRangeRecipe(type: RecipeType, otherUserId: Int) =
            if (type == RecipeType.ADDED) recipeDao.getRangeAddedRecipe(otherUserId)
            else recipeDao.getRangeUserRecipe(type.toString(), otherUserId)

    fun setRecipeProperty(recipe: Recipe, userId: Int? = null) {
        recipe.products = productDao.getByRecipeId(recipe.id!!)
        recipe.products!!.forEach { product ->
            product.weight = productDao.getProductWeight(recipe.id!!, product.id!!)
        }
        recipe.authorId?.let { recipe.author = userDao.getById(it) }
        userId?.let { recipe.hasYourLike = recipeDao.hasUserLike(recipe.id!!, userId) == 1 }
    }

    fun addRangeRecipesWithProperty(recipes: List<Recipe>) {
        for (recipe in recipes) {
            addRecipeWithProperty(recipe)
        }
    }

    fun addRangeRecipesWithProperty(recipes: List<Recipe>, recipeType: RecipeType, userId: Int, yourId: Int) {
        for (recipe in recipes) {
            addRecipeWithProperty(recipe)
            addUserRecipeWithUser(userId, recipe.id!!, recipeType)
            setOrRemoveLike(recipe.id!!, yourId, recipe.hasYourLike)
        }
    }

    fun setOrRemoveLike(recipeId: Int, userId: Int, hasLike: Boolean) {
        if (hasLike) addUserRecipeWithUser(userId, recipeId, RecipeType.LIKE)
        else userRecipeDao.remove(userId, recipeId, RecipeType.LIKE.toString())
    }

    private fun addRecipeWithProperty(recipe: Recipe) {
        userDao.add(recipe.author!!)
        recipe.authorId = recipe.author?.id
        recipeDao.add(recipe)
        recipe.products?.forEach { product ->
            productDao.add(product)
            if (recipeDao.hasProduct(recipe.id!!, product.id!!) == 0)
                productRecipeDao.add(ProductRecipe(null, recipe.id, product.id, product.weight))
            else productRecipeDao.update(recipe.id!!, product.id!!, product.weight!!)
        }
    }

    private fun addUserRecipeWithUser(userId: Int, recipeId: Int, recipeType: RecipeType) {
        if (userDao.hasRecipe(userId, recipeId, recipeType.toString()) == 0) {
            if (userDao.contains(userId) == 0)
                userDao.add(User(userId))
            userRecipeDao.add(UserRecipe(null, recipeId, userId, recipeType.toString()))
        }
    }
}