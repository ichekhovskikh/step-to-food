package com.sugar.steptofood.db.dao

import com.j256.ormlite.dao.Dao
import com.sugar.steptofood.db.LiteSqlHelper
import com.sugar.steptofood.db.dto.User

class UserDao private constructor() : EntityDao<User> {
    private var dao: Dao<User, Int> = LiteSqlHelper.getDao(User::class.java)

    private object Holder {
        val INSTANCE = UserDao()
    }

    companion object {
        val INSTANCE: UserDao by lazy { Holder.INSTANCE }
    }

    override fun get(id: Int) = dao.queryForId(id)

    override fun getAll() = dao.queryForAll()

    override fun add(entity: User) = dao.createOrUpdate(entity)

    override fun update(entity: User) = dao.update(entity)

    override fun remove(entity: User) = dao.delete(entity)

    override fun remove(id: Int) = dao.deleteById(id)

    override fun removeAll() {
        getAll().forEach { dao.delete(it) }
    }
}