package com.sugar.steptofood.paging.source

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.*
import com.sugar.steptofood.utils.LoadState
import com.sugar.steptofood.utils.RecipeType

class CacheUserRecipeSourceFactory(private val appDatabase: AppDatabase,
                                   private val session: Session,
                                   private val type: RecipeType,
                                   private val userId: Int) : StateSourceFactory<Int, Recipe>() {

    override fun create() = CacheUserRecipeSource()

    inner class CacheUserRecipeSource : PageKeyedDataSource<Int, Recipe>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Recipe>) {
            initialLoadState.postValue(LoadState.LOADING)
            val recipes = appDatabase.businessObject
                    .getRangeRecipe(type, userId, 0, params.requestedLoadSize)
            appDatabase.businessObject.setRangeRecipeProperty(recipes, session.userId)
            callback.onResult(recipes, 0, recipes.size)
            initialLoadState.postValue(LoadState.LOADED)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            callback.onResult(emptyList(), 0)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            additionalLoadState.postValue(LoadState.LOADING)
            val recipes = appDatabase.businessObject
                    .getRangeRecipe(type, userId, params.key, params.requestedLoadSize)
            appDatabase.businessObject.setRangeRecipeProperty(recipes, session.userId)
            callback.onResult(recipes, params.key + recipes.size)
            additionalLoadState.postValue(LoadState.LOADED)
        }
    }
}