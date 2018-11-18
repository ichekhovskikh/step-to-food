package com.sugar.steptofood.paging.source

import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.LoadState

class ComposedRecipeSourceFactory(private val api: ApiService,
                                  private val products: List<Product>) : StateSourceFactory<Int, Recipe>() {

    override fun create() = ComposedRecipeSource()

    inner class ComposedRecipeSource : PageKeyedDataSource<Int, Recipe>() {

        private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Recipe>) {
            initialLoadState.postValue(LoadState.LOADING)
            api.searchRecipesByProducts(productsId, 0, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, 0, recipes.size)
                        initialLoadState.postValue(LoadState.LOADED)
                    }, {
                        initialLoadState.postValue(LoadState.LOADED)
                    })
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {}

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {
            additionalLoadState.postValue(LoadState.LOADING)
            api.searchRecipesByProducts(productsId, params.key, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, params.key + recipes.size)
                        additionalLoadState.postValue(LoadState.LOADED)
                    }, {
                        additionalLoadState.postValue(LoadState.LOADED)
                    })
        }
    }
}