package com.sugar.steptofood.paging.factory

import com.sugar.steptofood.model.Product
import com.sugar.steptofood.paging.source.ComposedFoodSource
import com.sugar.steptofood.rest.ApiService
import io.reactivex.disposables.CompositeDisposable

class ComposedFoodSourceFactory(private val api: ApiService,
                                private val compositeDisposable: CompositeDisposable,
                                private val product: List<Product>) : BaseRecipeFactory() {

    override fun getDataSource() = ComposedFoodSource(api, compositeDisposable, product)
}