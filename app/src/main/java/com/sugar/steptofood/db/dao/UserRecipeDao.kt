package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.dto.UserRecipe

@Dao
interface UserRecipeDao : EntityDao<UserRecipe> {

    @Query("DELETE FROM userrecipe")
    fun removeAll()

    @Query("DELETE FROM userrecipe WHERE userId = :userId AND recipeId = :recipeId AND type = :type")
    fun remove(userId: Int, recipeId: Int, type: String)
}