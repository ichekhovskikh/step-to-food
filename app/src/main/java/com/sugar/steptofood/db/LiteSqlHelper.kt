package com.sugar.steptofood.db

import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.sugar.steptofood.App
import com.sugar.steptofood.db.dto.*

val DB_NAME = "steptofood.db"
val DB_VERSION = 1

object LiteSqlHelper : OrmLiteSqliteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource, User::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Product::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Food::class.java)
        TableUtils.createTableIfNotExists(connectionSource, ProductFood::class.java)
        TableUtils.createTableIfNotExists(connectionSource, LikeFood::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<LikeFood, Any>(connectionSource, LikeFood::class.java, true)
        TableUtils.dropTable<ProductFood, Any>(connectionSource, ProductFood::class.java, true)
        TableUtils.dropTable<User, Any>(connectionSource, User::class.java, true)
        TableUtils.dropTable<Product, Any>(connectionSource, Product::class.java, true)
        TableUtils.dropTable<Food, Any>(connectionSource, Food::class.java, true)
        onCreate(database, connectionSource)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
    }
}