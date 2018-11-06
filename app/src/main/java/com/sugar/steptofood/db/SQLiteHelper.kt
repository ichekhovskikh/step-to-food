package com.sugar.steptofood.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.sugar.steptofood.model.*
import javax.inject.Inject

class SQLiteHelper @Inject constructor(context: Context) : OrmLiteSqliteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "steptofood.db"
        const val DB_VERSION = 5
    }

    val userDao: EntityDao<User> by lazy { EntityDao(getConnectionSource(), User::class.java) }
    val foodDao: EntityDao<Food> by lazy { EntityDao(getConnectionSource(), Food::class.java) }
    val productDao: EntityDao<Product> by lazy { EntityDao(getConnectionSource(), Product::class.java) }
    val userFoodDao: EntityDao<UserFood> by lazy { EntityDao(getConnectionSource(), UserFood::class.java) }
    val productFoodDao: EntityDao<ProductFood> by lazy { EntityDao(getConnectionSource(), ProductFood::class.java) }
    val foodBusinessObject: FoodBusinessObject by lazy { FoodBusinessObject(this) }

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource, User::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Product::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Food::class.java)
        TableUtils.createTableIfNotExists(connectionSource, UserFood::class.java)
        TableUtils.createTableIfNotExists(connectionSource, ProductFood::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<UserFood, Any>(connectionSource, UserFood::class.java, true)
        TableUtils.dropTable<ProductFood, Any>(connectionSource, ProductFood::class.java, true)
        TableUtils.dropTable<Food, Any>(connectionSource, Food::class.java, true)
        TableUtils.dropTable<User, Any>(connectionSource, User::class.java, true)
        TableUtils.dropTable<Product, Any>(connectionSource, Product::class.java, true)
        onCreate(database, connectionSource)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
    }
}