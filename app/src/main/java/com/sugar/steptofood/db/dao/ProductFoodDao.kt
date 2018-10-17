package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.LiteSqlHelper
import com.sugar.steptofood.db.dto.ProductFood

class ProductFoodDao private constructor() : EntityDao<ProductFood> {
    private var dao: Dao<ProductFood, Int> = LiteSqlHelper.getDao(ProductFood::class.java)

    private object Holder {
        val INSTANCE = ProductFoodDao()
    }

    companion object {
        val INSTANCE: ProductFoodDao by lazy { Holder.INSTANCE }
    }

    override fun get(id: Int) = dao.queryForId(id)

    override fun getAll() = dao.queryForAll()

    override fun add(entity: ProductFood) = dao.createOrUpdate(entity)

    override fun update(entity: ProductFood) = dao.update(entity)

    override fun remove(entity: ProductFood) = dao.delete(entity)

    override fun remove(id: Int) = dao.deleteById(id)

    override fun removeAll() {
        getAll().forEach { dao.delete(it) }
    }
}