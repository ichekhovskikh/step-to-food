package com.sugar.steptofood.paging.factory

import android.arch.paging.DataSource
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.paging.source.ComposedFoodSource
import com.sugar.steptofood.rest.ApiService
import io.reactivex.disposables.CompositeDisposable

class ComposedFoodSourceFactory(private val api: ApiService,
                                private val compositeDisposable: CompositeDisposable,
                                private val product: List<Product>) : DataSource.Factory<Int, Food>() {

    override fun create(): DataSource<Int, Food> {
        return ComposedFoodSource(api, compositeDisposable, product)
    }
}