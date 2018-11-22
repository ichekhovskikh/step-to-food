package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.dto.EntityDto

@Dao
interface EntityDao<Dto : EntityDto> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: Dto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(entity: List<Dto>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: Dto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: List<Dto>)

    @Delete
    fun remove(entity: Dto)
}