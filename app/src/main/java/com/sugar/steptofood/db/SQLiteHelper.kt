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
        const val DB_VERSION = 7
    }

    val userDao: EntityDao<User> by lazy { EntityDao(getConnectionSource(), User::class.java) }
    val recipeDao: EntityDao<Recipe> by lazy { EntityDao(getConnectionSource(), Recipe::class.java) }
    val productDao: EntityDao<Product> by lazy { EntityDao(getConnectionSource(), Product::class.java) }
    val userRecipeDao: EntityDao<UserRecipe> by lazy { EntityDao(getConnectionSource(), UserRecipe::class.java) }
    val productRecipeDao: EntityDao<ProductRecipe> by lazy { EntityDao(getConnectionSource(), ProductRecipe::class.java) }
    val recipeBusinessObject: RecipeBusinessObject by lazy { RecipeBusinessObject(this) }

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource, User::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Product::class.java)
        TableUtils.createTableIfNotExists(connectionSource, Recipe::class.java)
        TableUtils.createTableIfNotExists(connectionSource, UserRecipe::class.java)
        TableUtils.createTableIfNotExists(connectionSource, ProductRecipe::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<UserRecipe, Any>(connectionSource, UserRecipe::class.java, true)
        TableUtils.dropTable<ProductRecipe, Any>(connectionSource, ProductRecipe::class.java, true)
        TableUtils.dropTable<Recipe, Any>(connectionSource, Recipe::class.java, true)
        TableUtils.dropTable<User, Any>(connectionSource, User::class.java, true)
        TableUtils.dropTable<Product, Any>(connectionSource, Product::class.java, true)
        onCreate(database, connectionSource)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
    }
}