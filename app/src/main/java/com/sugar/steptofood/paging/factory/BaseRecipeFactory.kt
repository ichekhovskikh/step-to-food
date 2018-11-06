package com.sugar.steptofood.paging.factory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PagedList
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.paging.source.BaseRecipeSource

abstract class BaseRecipeFactory : DataSource.Factory<Int, Food>() {

    private val mDataSource = MutableLiveData<BaseRecipeSource>()
    val currentDataSource: LiveData<BaseRecipeSource> = mDataSource

    protected abstract fun getDataSource(): BaseRecipeSource

    override fun create(): DataSource<Int, Food> {
        val dataSource = getDataSource()
        mDataSource.postValue(dataSource)
        return dataSource
    }

    open fun getNetworkSwapCallback() = object : PagedList.BoundaryCallback<Food>() { }
}