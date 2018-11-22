package com.sugar.steptofood.paging.source

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.sugar.steptofood.model.fullinfo.FullRecipeInfo
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.extension.smartSubscribe
import com.sugar.steptofood.utils.*

class NetworkTypeRecipeSourceFactory(private val api: ApiService,
                                     private val type: RecipeType,
                                     private val userId: Int,
                                     private val handleInitialLoadResponse: (List<FullRecipeInfo>) -> Unit,
                                     private val handleAdditionalLoadResponse: (List<FullRecipeInfo>) -> Unit)
    : StateSourceFactory<Int, FullRecipeInfo>() {

    override fun create() = NetworkUserRecipeSource()

    inner class NetworkUserRecipeSource : PageKeyedDataSource<Int, FullRecipeInfo>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, FullRecipeInfo>) {
            loadMore(0, params.requestedLoadSize, initialLoadState) { recipes, nextKey ->
                callback.onResult(recipes, 0, nextKey)
                handleInitialLoadResponse.invoke(recipes)
            }
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {
            callback.onResult(emptyList(), 0)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FullRecipeInfo>) {
            loadMore(params.key, params.requestedLoadSize, additionalLoadState) { recipes, nextKey ->
                callback.onResult(recipes, nextKey)
                handleAdditionalLoadResponse.invoke(recipes)
            }
        }

        private fun loadMore(start: Int, size: Int,
                             networkState: MutableLiveData<NetworkState>,
                             callback: (List<FullRecipeInfo>, Int) -> Unit) {
            networkState.postValue(NetworkState.LOADING)
            api.searchRecipes(userId, "", type, start, size)
                    .smartSubscribe({ recipes ->
                        callback.invoke(recipes, start + recipes.size)
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.error(it))
                    })
        }
    }
}