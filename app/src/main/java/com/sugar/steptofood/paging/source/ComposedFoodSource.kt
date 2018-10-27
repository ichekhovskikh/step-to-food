package com.sugar.steptofood.paging.source

import android.arch.paging.ItemKeyedDataSource
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService
import io.reactivex.disposables.CompositeDisposable

class ComposedFoodSource(private val api: ApiService,
                         private val compositeDisposable: CompositeDisposable,
                         private val products: List<Product>) : ItemKeyedDataSource<Int, Food>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProduct(products, params.requestedInitialKey!!, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError()
                    }
                }, { onError() }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProduct(products, params.key, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError()
                    }
                }, { onError() }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Food>) {}

    override fun getKey(item: Food) = item.id!!

    private fun onError() {
        //nothing
    }
}