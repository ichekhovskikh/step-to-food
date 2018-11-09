package com.sugar.steptofood.paging.factory

import com.sugar.steptofood.model.Product
import com.sugar.steptofood.paging.source.ComposedRecipeSource
import com.sugar.steptofood.rest.ApiService

class ComposedRecipeSourceFactory(private val api: ApiService,
                                  private val product: List<Product>) : BaseRecipeFactory() {

    override fun getDataSource() = ComposedRecipeSource(api, product)
}