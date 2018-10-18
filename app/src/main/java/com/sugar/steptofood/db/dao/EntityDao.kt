package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import com.sugar.steptofood.db.dto.Dto

class EntityDao<Entity : Dto> constructor(connectionSource: ConnectionSource,
                                          dataClass: Class<Entity>) : BaseDaoImpl<Entity, Int>(connectionSource, dataClass) {
    fun removeAll() {
        queryForAll().forEach { delete(it) }
    }
}