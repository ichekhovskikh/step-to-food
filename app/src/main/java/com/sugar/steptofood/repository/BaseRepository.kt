package com.sugar.steptofood.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

open class BaseRepository {
    protected val liveStatus = MutableLiveData<LoadingStatus>()
    protected val errorMessage = MutableLiveData<String>()

    fun loadingStatus(): LiveData<LoadingStatus> {
        return liveStatus
    }

    fun error(): MutableLiveData<String> {
        return errorMessage
    }

    protected fun onError() = { message: String ->
        liveStatus.postValue(LoadingStatus.LOADED)
        errorMessage.postValue(message)
    }

    enum class LoadingStatus {
        LOADING,
        LOADED
    }
}