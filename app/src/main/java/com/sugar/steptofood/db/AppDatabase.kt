package com.sugar.steptofood.db

import android.arch.persistence.room.*

import com.sugar.steptofood.db.dao.*
import com.sugar.steptofood.model.dto.*

@Database(entities = [User::class, Recipe::class, Product::class,
    UserRecipe::class, ProductRecipe::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun productRecipeDao(): ProductRecipeDao
    abstract fun userRecipeDao(): UserRecipeDao

    val businessLogicObject by lazy { RecipeBusinessLogicObject(recipeDao(),
            userDao(), productDao(), productRecipeDao(), userRecipeDao()) }
}
