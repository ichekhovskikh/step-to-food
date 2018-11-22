package com.sugar.steptofood.paging.source

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.utils.extension.smartSubscribe
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.NetworkState

class ComposedRecipeSourceFactory(private val api: ApiService,
                                  private val products: List<FullProductInfo>)
    : StateSourceFactory<Int, FullRecipeInfo>() {

    override fun create() = ComposedRecipeSource()

    inner class ComposedRecipeSource : PageKeyedDataSource<Int, FullRecipeInfo>() {

        private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, FullRecipeInfo>) {
            loadMore(0, params.requestedLoadSize, initialLoadState) { recipes, nextKey ->
                callback.onResult(recipes, 0, nextKey)
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {}

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {
            loadMore(params.key, params.requestedLoadSize, additionalLoadState) { recipes, nextKey ->
                callback.onResult(recipes, nextKey)
            }
        }

        private fun loadMore(start: Int, size: Int,
                             networkState: MutableLiveData<NetworkState>,
                             callback: (List<FullRecipeInfo>, Int) -> Unit) {
            networkState.postValue(NetworkState.LOADING)
            api.searchRecipesByProducts(productsId, start, size)
                    .smartSubscribe({ recipes ->
                        callback.invoke(recipes, start + recipes.size)
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.error(it))
                    })
        }
    }
}