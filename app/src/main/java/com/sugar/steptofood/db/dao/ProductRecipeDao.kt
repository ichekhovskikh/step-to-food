package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.*

@Dao
interface ProductRecipeDao : EntityDao<ProductRecipe> {

    @Query("SELECT * FROM productrecipe")
    fun getAll(): List<ProductRecipe>

    @Query("SELECT * FROM productrecipe WHERE productId = :productId and recipeId = :recipeId")
    fun getByUniqueRows(recipeId: Int, productId: Int): ProductRecipe?

    @Query("DELETE FROM productrecipe")
    fun removeAll()

    @Query("UPDATE productrecipe SET weight = :weight WHERE recipeId = :recipeId AND productId = :productId")
    fun update(recipeId: Int, productId: Int, weight: Int)
}