package com.sugar.steptofood.db.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.sugar.steptofood.model.*
import io.reactivex.Single

@Dao
interface RecipeDao : EntityDao<Recipe> {

    @Query("SELECT * FROM recipe")
    fun getAll(): DataSource.Factory<Int, Recipe>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getById(id: Int): Recipe

    @Query("DELETE FROM recipe")
    fun removeAll()

    @Query("DELETE FROM recipe WHERE  id = :id")
    fun remove(id: Int)

    @Query("SELECT COUNT(*) FROM userrecipe WHERE recipeId = :recipeId and userId = :userId and type = 'LIKE'")
    fun hasUserLike(recipeId: Int, userId: Int): Int

    @Query("SELECT COUNT(*) FROM productrecipe WHERE recipeId = :recipeId and productId = :productId")
    fun hasProduct(recipeId: Int, productId: Int): Int

    @Query("SELECT recipe.id, recipe.name, recipe.image, recipe.description, " +
            "recipe.calorie, recipe.protein, recipe.fat, recipe.carbohydrates, recipe.authorId " +
            "FROM userrecipe, recipe WHERE userrecipe.recipeId = recipe.id " +
            "AND userrecipe.userId = :userId " +
            "AND userrecipe.type = :type")
    fun getRangeUserRecipe(type: String, userId: Int): DataSource.Factory<Int, Recipe>

    @Query("SELECT * FROM recipe " +
            "WHERE authorId = :userId")
    fun getRangeAddedRecipe(userId: Int): DataSource.Factory<Int, Recipe>

    @Query("SELECT COUNT(*) FROM userrecipe, recipe " +
            "WHERE userrecipe.recipeId = recipe.id " +
            "AND userrecipe.userId = :userId " +
            "AND userrecipe.type = :type")
    fun count(userId: Int, type: String): Int
}