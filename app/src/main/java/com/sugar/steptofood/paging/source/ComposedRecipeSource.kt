package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService

class ComposedRecipeSource(private val api: ApiService,
                           products: List<Product>) : PositionalDataSource<Recipe>() {

    private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Recipe>) {
        api.searchRecipesByProducts(productsId, params.requestedStartPosition, params.requestedLoadSize)
                .customSubscribe({recipes ->
                    callback.onResult(recipes, if (recipes.isEmpty()) 0 else params.requestedLoadSize + 1)
                })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Recipe>) {
        api.searchRecipesByProducts(productsId, params.startPosition, params.loadSize)
                .customSubscribe({recipes ->
                    callback.onResult(recipes)
                })
    }
}