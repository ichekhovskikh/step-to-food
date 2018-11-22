package com.sugar.steptofood.paging.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.sugar.steptofood.utils.NetworkState

abstract class StateSourceFactory<K, V> : DataSource.Factory<K, V>() {
    protected var initialLoadState = MutableLiveData<NetworkState>()
    protected var additionalLoadState = MutableLiveData<NetworkState>()

    fun getInitialLoadState(): LiveData<NetworkState> = initialLoadState

    fun getAdditionalLoadState(): LiveData<NetworkState> = additionalLoadState
}