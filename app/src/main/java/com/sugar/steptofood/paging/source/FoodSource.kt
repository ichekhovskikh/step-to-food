package com.sugar.steptofood.paging.source

import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import io.reactivex.disposables.CompositeDisposable

class FoodSource(private val api: ApiService,
                 private val compositeDisposable: CompositeDisposable,
                 private val userId: Int,
                 private val type: FoodType,
                 private val dbHelper: SQLiteHelper?,
                 private val searchName: String) : BaseRecipeSource(api) {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .searchFoods(userId, searchName, type, params.requestedStartPosition, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        //dbHelper!!.foodBusinessObject.updateDb(response.content!!, type, userId)
                        callback.onResult(response.content!!,
                                if(response.content.isEmpty()) 0 else params.requestedLoadSize + 1)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        compositeDisposable.add(api
                .searchFoods(userId, searchName, type, params.startPosition, params.loadSize)
                .subscribe({ response ->
                    if (response.success) {
                        //dbHelper!!.foodBusinessObject.updateDb(response.content!!, type, userId)
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    private fun onError(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        //val foods = dbHelper!!.foodBusinessObject.getRangeFood(userId, searchName, type, params.requestedStartPosition, params.requestedLoadSize)
        //callback.onResult(foods, if (foods.isEmpty()) 0 else params.requestedLoadSize + 1)
    }

    private fun onError(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        //val foods = dbHelper!!.foodBusinessObject.getRangeFood(userId, searchName, type, params.startPosition, params.loadSize)
        //callback.onResult(foods)
    }
}