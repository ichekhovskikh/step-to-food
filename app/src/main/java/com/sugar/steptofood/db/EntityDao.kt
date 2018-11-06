package com.sugar.steptofood.db

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource

class EntityDao<Entity : com.sugar.steptofood.model.Entity>(connectionSource: ConnectionSource,
                                                            dataClass: Class<Entity>)
    : BaseDaoImpl<Entity, Int>(connectionSource, dataClass) {
    fun removeAll() {
        queryForAll().forEach { delete(it) }
    }

    fun removeRange(entities: List<Entity>) {
        queryForAll().forEach {
            if (entities.any { entity -> entity.id == it.id })
                delete(it)
        }
    }

    fun remove(id: Int) {
        queryForAll().forEach {
            if (it.id == id)
                delete(it)
        }
    }
}