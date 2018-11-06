package com.sugar.steptofood.paging.factory

import android.arch.paging.PagedList
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.paging.source.FoodSource
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.FoodType

class FoodSourceFactory(private val api: ApiService,
                        private val session: Session,
                        private val userId: Int,
                        private val type: FoodType,
                        private val dbHelper: SQLiteHelper,
                        private val searchName: String = "") : BaseRecipeFactory() {

    override fun getDataSource() = FoodSource(api, session, userId, type, dbHelper, searchName)

    override fun getNetworkSwapCallback() = FoodNetworkSwapCallback()

    inner class FoodNetworkSwapCallback : PagedList.BoundaryCallback<Food>() {

        override fun onZeroItemsLoaded() {
            loadItemFromNetwork()
        }

        override fun onItemAtEndLoaded(itemAtEnd: Food) {
            loadItemFromNetwork()
        }

        private fun loadItemFromNetwork() {
            if (searchName == "" /*so FoodSource.searchInNetworkWithoutCachingResult*/) {
                val start = dbHelper.foodBusinessObject.foodCount(type, userId)
                api.searchFoods(userId, searchName, type, start, NETWORK_PAGE_SIZE)
                        .customSubscribe({ foods ->
                            if (!foods.isEmpty()) {
                                dbHelper.foodBusinessObject.addFoods(foods, type, session.userId, userId)
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