package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService

abstract class BaseRecipeSource(private val api: ApiService,
                                private val dbHelper: SQLiteHelper? = null) : PositionalDataSource<Recipe>() {
    open fun removeItem(recipesId: Int, callback: () -> Unit) {
        api.removeRecipe(recipesId)
                .customSubscribe(
                        {
                            dbHelper?.recipeBusinessObject?.cascadeRemove(recipesId)
                            callback()
                        }, { }
                )
    }
}
