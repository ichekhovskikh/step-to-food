package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.*

@Dao
interface ProductDao : EntityDao<Product> {

    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getById(id: Int): Product

    @Query("DELETE FROM product")
    fun removeAll()

    @Query("SELECT product.id, product.name FROM product, productrecipe " +
            "WHERE productrecipe.productId = product.id and recipeId = :recipeId")
    fun getByRecipeId(recipeId: Int): List<Product>

    @Query("SELECT weight FROM productrecipe WHERE productId = :productId and recipeId = :recipeId")
    fun getProductWeight(recipeId: Int, productId: Int): Int
}