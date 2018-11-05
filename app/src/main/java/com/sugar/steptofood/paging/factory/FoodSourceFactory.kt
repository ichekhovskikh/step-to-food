package com.sugar.steptofood.paging.factory

import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.paging.source.FoodSource
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.FoodType
import io.reactivex.disposables.CompositeDisposable

class FoodSourceFactory(private val api: ApiService,
                        private val compositeDisposable: CompositeDisposable,
                        private val userId: Int,
                        private val type: FoodType,
                        private val dbHelper: SQLiteHelper? = null,
                        private val searchName: String = "") : BaseRecipeFactory() {

    override fun getDataSource() = FoodSource(api, compositeDisposable, userId, type, dbHelper, searchName)
}