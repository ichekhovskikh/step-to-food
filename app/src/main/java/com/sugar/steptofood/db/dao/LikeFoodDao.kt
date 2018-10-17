package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.LiteSqlHelper
import com.sugar.steptofood.db.dto.LikeFood

class LikeFoodDao private constructor() : EntityDao<LikeFood> {
    private var dao: Dao<LikeFood, Int> = LiteSqlHelper.getDao(LikeFood::class.java)

    private object Holder {
        val INSTANCE = LikeFoodDao()
    }

    companion object {
        val INSTANCE: LikeFoodDao by lazy { Holder.INSTANCE }
    }

    override fun get(id: Int) = dao.queryForId(id)

    override fun getAll() = dao.queryForAll()

    override fun add(entity: LikeFood) = dao.createOrUpdate(entity)

    override fun update(entity: LikeFood) = dao.update(entity)

    override fun remove(entity: LikeFood) = dao.delete(entity)

    override fun remove(id: Int) = dao.deleteById(id)

    override fun removeAll() {
        getAll().forEach { dao.delete(it) }
    }
}