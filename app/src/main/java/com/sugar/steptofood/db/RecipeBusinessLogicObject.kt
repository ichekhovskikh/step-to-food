package com.sugar.steptofood.db

import com.sugar.steptofood.db.dao.*
import com.sugar.steptofood.model.dto.*
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.utils.RecipeType
import com.sugar.steptofood.utils.extension.*

class RecipeBusinessLogicObject(private val recipeDao: RecipeDao,
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

    fun getRecipes(type: RecipeType, searchName: String, userId: Int, sessionUserId: Int, start: Int, size: Int) =
            if (type == RecipeType.ADDED) recipeDao.getAddedRecipes(searchName, userId, sessionUserId, start, size)
            else recipeDao.getUserRecipes(type.toString(), searchName, userId, sessionUserId, start, size)

    fun setLike(recipeId: Int, userId: Int, hasLike: Boolean) {
        if (hasLike) addUserRecipe(userId, recipeId, RecipeType.LIKE)
        else userRecipeDao.remove(userId, recipeId, RecipeType.LIKE.toString())
    }

    fun smartAddAllRecipes(recipes: List<FullRecipeInfo>, recipeType: RecipeType, userId: Int, sessionUserId: Int) {
        recipes.forEach { recipe ->
            smartAddRecipe(recipe, recipeType, userId, sessionUserId)
        }
    }

    fun smartAddRecipe(recipeInfo: FullRecipeInfo, recipeType: RecipeType, userId: Int, sessionUserId: Int) {
        val productRecipesForInsert = createProductRecipeList(recipeInfo)
        val products = recipeInfo.products?.toProductList()

        userDao.add(recipeInfo.author!!)
        recipeDao.add(recipeInfo.toRecipe())
        products?.let { productDao.add(it) }
        productRecipeDao.add(productRecipesForInsert)
        addUserRecipe(userId, recipeInfo.id!!, recipeType)

        setLike(recipeInfo.id!!, sessionUserId, recipeInfo.hasSessionUserLike)
    }

    private fun createProductRecipeList(recipeInfo: FullRecipeInfo) =
            recipeInfo.products?.map { product ->
                ProductRecipe(null, recipeInfo.id, product.id, product.weight)
            } ?: emptyList()

    private fun addUserRecipe(userId: Int, recipeId: Int, recipeType: RecipeType) {
        if (userDao.count(userId) == 0)
            userDao.add(User(userId))
        userRecipeDao.add(UserRecipe(null, recipeId, userId, recipeType.toString()))
    }
}