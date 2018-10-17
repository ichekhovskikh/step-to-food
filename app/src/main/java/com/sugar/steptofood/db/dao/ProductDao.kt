package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.LiteSqlHelper
import com.sugar.steptofood.db.dto.Product

class ProductDao private constructor() : EntityDao<Product> {
    private var dao: Dao<Product, Int> = LiteSqlHelper.getDao(Product::class.java)

    private object Holder {
        val INSTANCE = ProductDao()
    }

    companion object {
        val INSTANCE: ProductDao by lazy { Holder.INSTANCE }
    }

    override fun get(id: Int) = dao.queryForId(id)

    override fun getAll() = dao.queryForAll()

    override fun add(entity: Product) = dao.createOrUpdate(entity)

    override fun update(entity: Product) = dao.update(entity)

    override fun remove(entity: Product) = dao.delete(entity)

    override fun remove(id: Int) = dao.deleteById(id)

    override fun removeAll() {
        getAll().forEach { dao.delete(it) }
    }
}