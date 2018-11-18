package com.sugar.steptofood.paging.source

import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.LoadState
import com.sugar.steptofood.utils.RecipeType

class NetworkUserRecipeSourceFactory(private val api: ApiService,
                                     private val appDatabase: AppDatabase,
                                     private val session: Session,
                                     private val type: RecipeType,
                                     private val userId: Int) : StateSourceFactory<Int, Recipe>() {

    override fun create() = NetworkUserRecipeSource()

    inner class NetworkUserRecipeSource : PageKeyedDataSource<Int, Recipe>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Recipe>) {
            initialLoadState.postValue(LoadState.LOADING)
            api.searchRecipes(userId, "", type, 0, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        if (recipes.isNotEmpty()) {
                            appDatabase.businessObject.removeAll()
                            appDatabase.businessObject.addRangeRecipesWithProperty(recipes, type, userId, session.userId)
                            callback.onResult(recipes, 0, recipes.size)
                        }
                        initialLoadState.postValue(LoadState.LOADED)
                    }, {
                        initialLoadState.postValue(LoadState.LOADED)
                    })
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            callback.onResult(emptyList(), 0)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            additionalLoadState.postValue(LoadState.LOADING)
            api.searchRecipes(userId, "", type, params.key, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        appDatabase.businessObject.addRangeRecipesWithProperty(recipes, type, userId, session.userId)
                        callback.onResult(recipes, params.key + recipes.size)
                        additionalLoadState.postValue(LoadState.LOADED)
                    }, {
                        additionalLoadState.postValue(LoadState.LOADED)
                    })
        }
    }
}