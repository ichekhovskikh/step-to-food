package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.*

@Dao
interface UserDao : EntityDao<User> {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): User

    @Query("DELETE FROM user")
    fun removeAll()

    @Query("SELECT COUNT(*) FROM userrecipe WHERE recipeId = :recipeId and userId = :userId and type = :type")
    fun hasRecipe(userId: Int, recipeId: Int, type: String): Int

    @Query("SELECT COUNT(*) FROM user WHERE id = :id")
    fun contains(id: Int): Int
}