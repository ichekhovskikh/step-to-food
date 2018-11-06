package com.sugar.steptofood.paging.source

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService

class ComposedFoodSource(private val api: ApiService,
                         products: List<Product>) : BaseRecipeSource(api) {

    private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        api.searchFoodsByProducts(productsId, params.requestedStartPosition, params.requestedLoadSize)
                .customSubscribe({foods ->
                    callback.onResult(foods, if (foods.isEmpty()) 0 else params.requestedLoadSize + 1)
                })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        api.searchFoodsByProducts(productsId, params.startPosition, params.loadSize)
                .customSubscribe({foods ->
                    callback.onResult(foods)
                })
    }
}