package com.sugar.steptofood.paging.factory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PagedList
import android.arch.paging.PositionalDataSource
import com.sugar.steptofood.model.Recipe

abstract class BaseRecipeFactory : DataSource.Factory<Int, Recipe>() {

    private val mDataSource = MutableLiveData<PositionalDataSource<Recipe>>()
    val currentDataSource: LiveData<PositionalDataSource<Recipe>> = mDataSource

    protected abstract fun getDataSource(): PositionalDataSource<Recipe>

    override fun create(): DataSource<Int, Recipe> {
        val dataSource = getDataSource()
        mDataSource.postValue(dataSource)
        return dataSource
    }

    open fun getNetworkSwapCallback() = object : PagedList.BoundaryCallback<Recipe>() { }
}