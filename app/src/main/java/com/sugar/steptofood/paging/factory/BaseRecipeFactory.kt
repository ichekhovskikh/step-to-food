package com.sugar.steptofood.paging.factory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PagedList
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.paging.source.BaseRecipeSource

abstract class BaseRecipeFactory : DataSource.Factory<Int, Recipe>() {

    private val mDataSource = MutableLiveData<BaseRecipeSource>()
    val currentDataSource: LiveData<BaseRecipeSource> = mDataSource

    protected abstract fun getDataSource(): BaseRecipeSource

    override fun create(): DataSource<Int, Recipe> {
        val dataSource = getDataSource()
        mDataSource.postValue(dataSource)
        return dataSource
    }

    open fun getNetworkSwapCallback() = object : PagedList.BoundaryCallback<Recipe>() { }
}