package com.sugar.steptofood.paging.source

import android.arch.paging.ItemKeyedDataSource
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import io.reactivex.disposables.CompositeDisposable

class FoodSource(private val api: ApiService,
                 private val compositeDisposable: CompositeDisposable,
                 private val userId: Int,
                 private val type: FoodType,
                 private val dbHelper: SQLiteHelper?) : ItemKeyedDataSource<Int, Food>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.requestedInitialKey!!, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        dbHelper!!.foodBusinessObject.update(response.content!!, type)
                        callback.onResult(response.content)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.key, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        dbHelper!!.foodBusinessObject.update(response.content!!, type)
                        callback.onResult(response.content)
                    } else if (!response.success) {
                        onError(params, callback)
                    }
                }, { onError(params, callback) }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Food>) {}

    override fun getKey(item: Food) = item.id!!

    private fun onError(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Food>) {
        callback.onResult(dbHelper!!.foodBusinessObject.getRangeFood(userId, type, params.requestedInitialKey!!, params.requestedLoadSize))
    }

    private fun onError(params: LoadParams<Int>, callback: LoadCallback<Food>) {
        callback.onResult(dbHelper!!.foodBusinessObject.getRangeFood(userId, type, params.key, params.requestedLoadSize))
    }
}