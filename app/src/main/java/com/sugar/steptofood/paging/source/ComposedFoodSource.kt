package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService
import io.reactivex.disposables.CompositeDisposable

class ComposedFoodSource(private val api: ApiService,
                         private val compositeDisposable: CompositeDisposable,
                         products: List<Product>) : PositionalDataSource<Food>() {

    private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProducts(productsId, params.requestedStartPosition, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!, params.requestedLoadSize + 1)
                    } else if (!response.success) {
                        onError()
                    }
                }, { onError() }))
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProducts(productsId, params.startPosition, params.loadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError()
                    }
                }, { onError() }))
    }

    private fun onError() {
        //nothing
    }
}