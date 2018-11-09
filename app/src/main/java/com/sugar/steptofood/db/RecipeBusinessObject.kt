package com.sugar.steptofood.db

import com.sugar.steptofood.model.*
import com.sugar.steptofood.utils.RecipeType
import kotlin.math.min

class RecipeBusinessObject(private val dbHelper: SQLiteHelper) {

    fun removeAll() {
        dbHelper.userRecipeDao.removeAll()
        dbHelper.userDao.removeAll()
        dbHelper.productRecipeDao.removeAll()
        dbHelper.productDao.removeAll()
        dbHelper.recipeDao.removeAll()
    }

    fun cascadeRemove(recipeId: Int) {
        val userRecipes = dbHelper.userRecipeDao.filter { userRecipe ->
            userRecipe.recipe?.id == recipeId
        }
        dbHelper.userRecipeDao.removeRange(userRecipes)

        val productRecipes = dbHelper.productRecipeDao.filter { productRecipe ->
            productRecipe.recipe?.id == recipeId
        }
        dbHelper.productRecipeDao.removeRange(productRecipes)
        dbHelper.recipeDao.remove(recipeId)
    }

    fun recipeCount(type: RecipeType, userId: Int) =
            getRangeRecipe(type, null, userId, "", 0, Int.MAX_VALUE).count()

    fun getRangeRecipe(type: RecipeType, yourId: Int?, otherUserId: Int, searchName: String, start: Int, size: Int) =
            if (type == RecipeType.ADDED)
                getRangeAddedRecipe(yourId, otherUserId, searchName, start, size)
            else getRangeUserRecipe(type, yourId, otherUserId, searchName, start, size)

    fun addRecipes(recipes: List<Recipe>, recipeType: RecipeType = RecipeType.ADDED, yourId: Int = -1, userId: Int = -1) {
        if (recipeType == RecipeType.ADDED) putAddedRecipe(recipes)
        else putUserRecipe(recipeType, recipes, yourId, userId)
    }

    fun putAddedRecipe(recipes: List<Recipe>) {
        for (recipe in recipes) {
            createOrUpdateRecipe(recipe)
        }
    }

    fun putUserRecipe(recipeType: RecipeType, recipes: List<Recipe>, yourId: Int, userId: Int) {
        for (recipe in recipes) {
            createOrUpdateRecipe(recipe)
            createUserRecipe(recipeType, recipe.id!!, userId)
            setOrRemoveLike(recipe.id!!, yourId, recipe.hasYourLike)
        }
    }

    fun setOrRemoveLike(recipeId: Int, userId: Int, hasLike: Boolean) {
        if (hasLike)
            createUserRecipe(RecipeType.LIKE, recipeId, userId)
        else if (hasUserLike(userId, recipeId)) {
            val likeRecipe = dbHelper.userRecipeDao.first {
                userRecipeContentEquals(RecipeType.LIKE, recipeId, userId, it)
            }
            dbHelper.userRecipeDao.remove(likeRecipe.id!!)
        }
    }

    fun recipeContainsProduct(recipe: Recipe, product: Product) =
            dbHelper.productRecipeDao.any { productRecipe ->
                productRecipe.recipe?.id == recipe.id && productRecipe.product?.id == product.id
            }

    fun hasUserLike(userId: Int, recipeId: Int) =
            dbHelper.userRecipeDao.any { likeRecipe ->
                likeRecipe.recipe?.id == recipeId
                        && likeRecipe.user?.id == userId
                        && likeRecipe.type == RecipeType.LIKE.toString()
            }

    private fun getRangeUserRecipe(type: RecipeType,
                                   yourId: Int?,
                                   userId: Int,
                                   searchName: String,
                                   start: Int,
                                   size: Int): List<Recipe> {
        val userRecipes = dbHelper.userRecipeDao.filter { userRecipe ->
            userRecipe.user?.id == userId
                    && userRecipe.recipe!!.name.contains(searchName)
                    && userRecipe.type == type.toString()
        }

        val recipes = mutableListOf<Recipe>()
        for (i in start until min(userRecipes.count(), size)) {
            val recipe = userRecipes[i].recipe!!
            setRecipeProperty(recipe, yourId, userId)
            recipes.add(recipe)
        }
        return recipes
    }

    private fun getRangeAddedRecipe(yourId: Int?,
                                    userId: Int,
                                    searchName: String,
                                    start: Int,
                                    size: Int): List<Recipe> {
        val addedRecipes = dbHelper.recipeDao.filter { recipe ->
            recipe.author?.id == userId && recipe.name.contains(searchName)
        }

        val recipes = mutableListOf<Recipe>()
        for (i in start until min(addedRecipes.count(), size)) {
            val recipe = addedRecipes[i]
            setRecipeProperty(recipe, yourId, userId)
            recipes.add(recipe)
        }
        return recipes
    }

    private fun setRecipeProperty(recipe: Recipe, yourId: Int?, userId: Int) {
        val products = dbHelper.productDao.filter { product ->
            recipeContainsProduct(recipe, product)
        }
        recipe.products = products
        if (yourId != null)
            recipe.hasYourLike = hasUserLike(yourId, recipe.id!!)
    }

    private fun createOrUpdateRecipe(recipe: Recipe) {
        dbHelper.userDao.createOrUpdate(recipe.author)
        dbHelper.recipeDao.createOrUpdate(recipe)
        recipe.products?.forEach { product ->
            dbHelper.productDao.createOrUpdate(product)
            createProductRecipe(product, recipe)
        }
    }

    private fun createUserRecipe(recipeType: RecipeType, recipeId: Int, userId: Int) {
        if (dbHelper.userRecipeDao.none { userRecipe -> userRecipeContentEquals(recipeType, recipeId, userId, userRecipe) }) {
            dbHelper.userDao.createIfNotExists(User(userId))
            val recipeFromDb = dbHelper.recipeDao.queryForId(recipeId)
            val userFromDb = dbHelper.userDao.queryForId(userId)
            dbHelper.userRecipeDao.create(UserRecipe(null, recipeFromDb, userFromDb, recipeType.toString()))
        }
    }

    private fun createProductRecipe(product: Product, recipe: Recipe) {
        if (dbHelper.productRecipeDao.none { productRecipe -> productRecipeContentEquals(recipe, product, productRecipe) }) {
            val recipeFromDb = dbHelper.recipeDao.queryForId(recipe.id)
            val productFromDb = dbHelper.productDao.queryForId(product.id)
            dbHelper.productRecipeDao.create(ProductRecipe(null, recipeFromDb, productFromDb))
        }
    }

    private fun productRecipeContentEquals(recipe: Recipe, product: Product, productRecipe: ProductRecipe) =
            productRecipe.recipe?.id == recipe.id && productRecipe.product?.id == product.id

    private fun userRecipeContentEquals(recipeType: RecipeType, recipeId: Int, userId: Int, userRecipe: UserRecipe) =
            userRecipe.recipe?.id == recipeId && userRecipe.user?.id == userId && userRecipe.type == recipeType.toString()
}