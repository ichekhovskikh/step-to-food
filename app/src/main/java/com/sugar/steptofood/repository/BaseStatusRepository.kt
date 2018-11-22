package com.sugar.steptofood.repository

import android.arch.lifecycle.*
import com.sugar.steptofood.utils.NetworkState

open class BaseStatusRepository {
    protected val status = MutableLiveData<NetworkState>()

    fun loadingStatus(): LiveData<NetworkState> = status

    protected fun onError() = { message: String ->
        status.postValue(NetworkState.error(message))
    }
}