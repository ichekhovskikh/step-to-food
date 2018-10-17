package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.dto.Dto

interface EntityDao<Entity : Dto> {

    fun get(id: Int): Entity?

    fun getAll(): MutableList<Entity>?

    fun add(entity: Entity): Dao.CreateOrUpdateStatus?

    fun update(entity: Entity): Int

    fun remove(entity: Entity): Int

    fun remove(id: Int): Int

    fun removeAll()
}