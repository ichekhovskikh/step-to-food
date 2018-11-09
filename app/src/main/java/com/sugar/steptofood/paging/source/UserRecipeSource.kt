package com.sugar.steptofood.paging.source

import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*

class UserRecipeSource(private val api: ApiService,
                       private val session: Session,
                       private val dbHelper: SQLiteHelper,
                       private val userId: Int,
                       private val type: RecipeType,
                       private val searchName: String) : BaseRecipeSource(api, dbHelper) {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Recipe>) {
        loadData(params.requestedStartPosition, params.requestedLoadSize) { recipes ->
            callback.onResult(recipes, 0)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Recipe>) {
        loadData(params.startPosition, params.loadSize) { recipes ->
            callback.onResult(recipes)
        }
    }

    private fun loadData(start: Int, size: Int, callback: (List<Recipe>) -> Unit) {
        if (searchName != "") {
            searchWithoutCachingResult(start, size) { recipes: List<Recipe> ->
                callback.invoke(recipes)
            }
        } else {
            val recipes = dbHelper.recipeBusinessObject.getRangeRecipe(type,
                    session.userId, userId, searchName, start, size)
            callback.invoke(recipes)
        }
    }

    private fun searchWithoutCachingResult(start: Int, size: Int, callback: (List<Recipe>) -> Unit) {
        api.searchRecipes(userId, searchName, type, start, size)
                .customSubscribe({ recipes ->
                    callback.invoke(recipes)
                }, {
                    val recipes = dbHelper.recipeBusinessObject.getRangeRecipe(type,
                            session.userId, userId, searchName, start, size)
                    callback.invoke(recipes)
                })
    }
}