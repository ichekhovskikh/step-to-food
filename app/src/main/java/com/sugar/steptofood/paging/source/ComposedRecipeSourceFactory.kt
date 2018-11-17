package com.sugar.steptofood.paging.source

import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.ApiService

class ComposedRecipeSourceFactory(private val api: ApiService,
                                  private val products: List<Product>) : DataSource.Factory<Int, Recipe>() {

    override fun create(): DataSource<Int, Recipe> {
        return ComposedRecipeSource()
    }

    inner class ComposedRecipeSource : PositionalDataSource<Recipe>() {
        private val productsId: List<Int> = products.asSequence().map { it.id!! }.toList()

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Recipe>) {
            api.searchRecipesByProducts(productsId, params.requestedStartPosition, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, recipes.size)
                    })
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Recipe>) {
            if (isLoadBefore(params.startPosition))
                return

            api.searchRecipesByProducts(productsId, params.startPosition, params.loadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes)
                    })
        }

        private fun isLoadBefore(start: Int) = start == 0
    }
}