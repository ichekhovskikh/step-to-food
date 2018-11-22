package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.dto.Product

@Dao
interface ProductDao : EntityDao<Product> {

    @Query("DELETE FROM product")
    fun removeAll()
}