package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService
import io.reactivex.disposables.CompositeDisposable

class ComposedFoodSource internal constructor(private val api: ApiService,
                                              private val compositeDisposable: CompositeDisposable,
                                              private val products: List<Product>) : PositionalDataSource<Food>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProduct(products)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!, params.requestedStartPosition)
                    } else if (!response.success) {
                        onError()
                    }
                }, { throwable -> onError() }))
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        compositeDisposable.add(api
                .searchFoodsByProduct(products)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError()
                    }
                }, { throwable -> onError() }))
    }

    private fun onError() {}
}