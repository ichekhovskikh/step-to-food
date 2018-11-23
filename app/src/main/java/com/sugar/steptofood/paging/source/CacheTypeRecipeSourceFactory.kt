package com.sugar.steptofood.paging.source

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.model.fullinfo.FullRecipeInfo
import com.sugar.steptofood.utils.NetworkState

class CacheTypeRecipeSourceFactory(private val onLoadMoreFromCache: (start: Int, size: Int, (List<FullRecipeInfo>) -> Unit) -> Unit)
    : StateSourceFactory<Int, FullRecipeInfo>() {

    override fun create() = CacheUserRecipeSource()

    inner class CacheUserRecipeSource : PageKeyedDataSource<Int, FullRecipeInfo>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, FullRecipeInfo>) {
            loadMore(0, params.requestedLoadSize, initialLoadState) { recipes, nextKey ->
                callback.onResult(recipes, 0, nextKey)
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {
            callback.onResult(emptyList(), 0)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {
            loadMore(params.key, params.requestedLoadSize, additionalLoadState) { recipes, nextKey ->
                callback.onResult(recipes, nextKey)
            }
        }

        private fun loadMore(start: Int, size: Int,
                             networkState: MutableLiveData<NetworkState>,
                             callback: (List<FullRecipeInfo>, Int) -> Unit) {
            networkState.postValue(NetworkState.LOADING)
            onLoadMoreFromCache.invoke(start, size) { recipes ->
                callback.invoke(recipes, start + recipes.size)
                networkState.postValue(NetworkState.LOADED)
            }
        }
    }
}