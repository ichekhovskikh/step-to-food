package com.sugar.steptofood.db.dao

import android.arch.persistence.room.*
import com.sugar.steptofood.model.dto.User

@Dao
interface UserDao : EntityDao<User> {

    @Query("DELETE FROM user")
    fun removeAll()

    @Query("SELECT COUNT(*) FROM user WHERE id = :id")
    fun count(id: Int): Int
}