package com.sugar.steptofood.paging

import android.arch.paging.PagedList
import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.utils.RecipeType

class RecipeNetworkSwapCallback(private val api: ApiService,
                                private val appDatabase: AppDatabase,
                                private val session: Session,
                                private val type: RecipeType,
                                private val userId: Int,
                                private val pageSize: Int) : PagedList.BoundaryCallback<Recipe>() {

    private var isLoadedFrontItem = false
    private var isLoadedEndItem = false

    override fun onItemAtFrontLoaded(itemAtFront: Recipe) {
        if (isLoadedFrontItem)
            return

        api.searchRecipes(userId, "", type, 0, pageSize)
                .customSubscribe({ recipes ->
                    if (recipes.isNotEmpty()) {
                        isLoadedFrontItem = true
                        appDatabase.businessObject.removeAll()
                        appDatabase.businessObject.addRangeRecipesWithProperty(recipes, type, userId, session.userId)
                    }
                })
    }

    override fun onZeroItemsLoaded() {
        loadItemFromNetwork()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Recipe) {
        loadItemFromNetwork()
    }

    private fun loadItemFromNetwork() {
        if (isLoadedEndItem)
            return

        val start = appDatabase.recipeDao().count(userId, type.toString())
        api.searchRecipes(userId, "", type, start, pageSize)
                .customSubscribe({ recipes ->
                    if (recipes.isNotEmpty()) {
                        appDatabase.businessObject.addRangeRecipesWithProperty(recipes, type, userId, session.userId)
                    } else isLoadedEndItem = true
                })
    }
}