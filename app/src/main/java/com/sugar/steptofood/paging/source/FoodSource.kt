package com.sugar.steptofood.paging.source

import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*

class FoodSource(private val api: ApiService,
                 private val session: Session,
                 private val userId: Int,
                 private val type: FoodType,
                 private val dbHelper: SQLiteHelper,
                 private val searchName: String) : BaseRecipeSource(api, dbHelper) {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        if (searchName != "") {
            searchInNetworkWithoutCachingResult(params.requestedStartPosition, params.requestedLoadSize) { foods: List<Food>, position: Int ->
                callback.onResult(foods, position)
            }
        } else {
            val foods = dbHelper.foodBusinessObject.getRangeFood(type, session.userId,
                    userId, searchName, params.requestedStartPosition, params.requestedLoadSize)
            callback.onResult(foods, foods.count())
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        if (searchName != "") {
            searchInNetworkWithoutCachingResult(params.startPosition, params.loadSize) { foods: List<Food>, _: Int ->
                callback.onResult(foods)
            }
        } else callback.onResult(dbHelper.foodBusinessObject.getRangeFood(type,
                session.userId, userId, searchName, params.startPosition, params.loadSize))
    }

    private fun searchInNetworkWithoutCachingResult(start: Int, size: Int, callback: (List<Food>, Int) -> Unit) {
        api.searchFoods(userId, searchName, type, start, size)
                .customSubscribe({ foods ->
                    callback.invoke(foods, if (foods.isEmpty()) 0 else size + 1)
                })
    }
}