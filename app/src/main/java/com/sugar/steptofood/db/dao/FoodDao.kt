package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.LiteSqlHelper
import com.sugar.steptofood.db.dto.Food

class FoodDao private constructor() : EntityDao<Food> {
    private var dao: Dao<Food, Int> = LiteSqlHelper.getDao(Food::class.java)

    private object Holder {
        val INSTANCE = FoodDao()
    }

    companion object {
        val INSTANCE: FoodDao by lazy { Holder.INSTANCE }
    }

    override fun get(id: Int) = dao.queryForId(id)

    override fun getAll() = dao.queryForAll()

    override fun add(entity: Food) = dao.createOrUpdate(entity)

    override fun update(entity: Food) = dao.update(entity)

    override fun remove(entity: Food) = dao.delete(entity)

    override fun remove(id: Int) = dao.deleteById(id)

    override fun removeAll() {
        getAll().forEach { dao.delete(it) }
    }
}