package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.EntityDto

@Dao
interface EntityDao<Dto : EntityDto> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: Dto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: Dto)

    @Delete
    fun remove(entity: Dto)
}