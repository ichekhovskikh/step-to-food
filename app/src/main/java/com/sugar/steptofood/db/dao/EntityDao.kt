package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource

class EntityDao<Entity : com.sugar.steptofood.model.Entity> constructor(connectionSource: ConnectionSource,
                                                                        dataClass: Class<Entity>) : BaseDaoImpl<Entity, Int>(connectionSource, dataClass) {
    fun removeAll() {
        queryForAll().forEach { delete(it) }
    }
}