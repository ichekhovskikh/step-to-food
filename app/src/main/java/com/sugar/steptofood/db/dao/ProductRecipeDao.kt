package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.dto.ProductRecipe

@Dao
interface ProductRecipeDao : EntityDao<ProductRecipe> {

    @Query("DELETE FROM productrecipe")
    fun removeAll()
}