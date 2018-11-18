package com.sugar.steptofood.paging.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.sugar.steptofood.utils.LoadState

abstract class StateSourceFactory<K, V> : DataSource.Factory<K, V>() {
    protected var initialLoadState: MutableLiveData<LoadState> = MutableLiveData()
    protected var additionalLoadState: MutableLiveData<LoadState> = MutableLiveData()

    init {
        initialLoadState.postValue(LoadState.LOADING)
        additionalLoadState.postValue(LoadState.LOADED)
    }

    fun getInitialLoadState(): LiveData<LoadState> = initialLoadState

    fun getAdditionalLoadState(): LiveData<LoadState> = additionalLoadState
}