package com.sugar.steptofood.paging.factory

import android.arch.paging.DataSource
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.paging.source.FoodSource
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.FoodType
import io.reactivex.disposables.CompositeDisposable

class FoodSourceFactory(private val api: ApiService,
                        private val compositeDisposable: CompositeDisposable,
                        private val userId: Int,
                        private val type: FoodType,
                        private val dbHelper: SQLiteHelper? = null,
                        private val searchName: String = "") : DataSource.Factory<Int, Food>() {

    override fun create(): DataSource<Int, Food> {
        return FoodSource(api, compositeDisposable, userId, type, dbHelper, searchName)
    }
}