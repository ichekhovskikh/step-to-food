package com.sugar.steptofood.paging.factory

import android.arch.paging.PagedList
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.paging.source.UserRecipeSource
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.RecipeType

class UserRecipeSourceFactory(private val api: ApiService,
                              private val session: Session,
                              private val dbHelper: SQLiteHelper,
                              private val userId: Int,
                              private val type: RecipeType,
                              private val searchName: String = "") : BaseRecipeFactory() {

    override fun getDataSource() = UserRecipeSource(api, session, dbHelper, userId, type, searchName)

    override fun getNetworkSwapCallback() = RecipeNetworkSwapCallback()

    inner class RecipeNetworkSwapCallback : PagedList.BoundaryCallback<Recipe>() {

        override fun onZeroItemsLoaded() {
            loadItemFromNetwork()
        }

        override fun onItemAtEndLoaded(itemAtEnd: Recipe) {
            loadItemFromNetwork()
        }

        private fun loadItemFromNetwork() {
            if (searchName == "" /*so UserRecipeSource.searchWithoutCachingResult*/) {
                val start = dbHelper.recipeBusinessObject.recipeCount(type, userId)
                api.searchRecipes(userId, searchName, type, start, NETWORK_PAGE_SIZE)
                        .customSubscribe({ recipes ->
                            if (!recipes.isEmpty()) {
                                dbHelper.recipeBusinessObject.addRecipes(recipes, type, session.userId, userId)
                                currentDataSource.value?.invalidate()
                            }
                        })
            }
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}