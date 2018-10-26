package com.sugar.steptofood.paging.source

import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.FoodType
import io.reactivex.disposables.CompositeDisposable

class FoodSource internal constructor(private val api: ApiService,
                                      private val compositeDisposable: CompositeDisposable,
                                      private val userId: Int,
                                      private val type: FoodType) : PositionalDataSource<Food>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.requestedStartPosition, params.requestedLoadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!, params.requestedStartPosition)
                    } else if (!response.success) {
                        onError()
                    }
                }, { throwable -> onError() }))
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Food>) {
        compositeDisposable.add(api
                .getFoodAll(userId, type, params.startPosition, params.loadSize)
                .subscribe({ response ->
                    if (response.success) {
                        callback.onResult(response.content!!)
                    } else if (!response.success) {
                        onError()
                    }
                }, { throwable -> onError() }))
    }

    private fun onError() {}
}