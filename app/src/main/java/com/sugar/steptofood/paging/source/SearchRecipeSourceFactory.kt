package com.sugar.steptofood.paging.source

import android.arch.paging.DataSource
import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.RecipeType

class SearchRecipeSourceFactory(private val api: ApiService,
                                private val type: RecipeType,
                                private val userId: Int,
                                private val searchName: String) : DataSource.Factory<Int, Recipe>() {

    override fun create(): DataSource<Int, Recipe> {
        return ComposedRecipeSource()
    }

    inner class ComposedRecipeSource : PositionalDataSource<Recipe>() {

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Recipe>) {
            api.searchRecipes(userId, searchName, type, params.requestedStartPosition, params.requestedLoadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes, recipes.size)
                    })
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Recipe>) {
            if (isLoadBefore(params.startPosition))
                return

            api.searchRecipes(userId, searchName, type, params.startPosition, params.loadSize)
                    .customSubscribe({ recipes ->
                        callback.onResult(recipes)
                    })
        }

        private fun isLoadBefore(start: Int) = start == 0
    }
}