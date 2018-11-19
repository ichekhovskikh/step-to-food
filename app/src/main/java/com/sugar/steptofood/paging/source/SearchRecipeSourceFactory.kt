package com.sugar.steptofood.paging.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.LoadState
import com.sugar.steptofood.utils.RecipeType

class SearchRecipeSourceFactory(private val api: ApiService,
                                private val type: RecipeType,
                                private val userId: Int,
                                private val searchName: String) : StateSourceFactory<Int, Recipe>() {

    override fun create() = SearchRecipeSource()

    inner class SearchRecipeSource : PageKeyedDataSource<Int, Recipe>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Recipe>) {
            initialLoadState.postValue(LoadState.LOADING)
            api.searchRecipes(userId, searchName, type, 0, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, 0, recipes.size)
                        initialLoadState.postValue(LoadState.LOADED)
                    }, {
                        initialLoadState.postValue(LoadState.ERROR)
                    })
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            callback.onResult(emptyList(), 0)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            additionalLoadState.postValue(LoadState.LOADING)
            api.searchRecipes(userId, searchName, type, params.key, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, params.key + recipes.size)
                        additionalLoadState.postValue(LoadState.LOADED)
                    }, {
                        additionalLoadState.postValue(LoadState.ERROR)
                    })
        }
    }
}