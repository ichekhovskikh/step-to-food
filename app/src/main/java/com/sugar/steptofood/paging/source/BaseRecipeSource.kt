package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService

abstract class BaseRecipeSource(private val api: ApiService,
                                private val dbHelper: SQLiteHelper? = null) : PositionalDataSource<Food>() {
    open fun removeItem(foodId: Int, callback: () -> Unit) {
        api.removeFood(foodId)
                .customSubscribe(
                        {
                            dbHelper?.foodBusinessObject?.cascadeRemove(foodId)
                            callback()
                        }, { }
                )
    }
}
