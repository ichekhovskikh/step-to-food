package com.sugar.steptofood.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

open class BaseRepository {
    protected val liveStatus = MutableLiveData<LoadingStatus>()
    protected val errorMessage = MutableLiveData<String>()

    fun loadingStatus(): LiveData<LoadingStatus> {
        return liveStatus
    }

    protected fun error(): LiveData<String> {
        return errorMessage
    }

    enum class LoadingStatus {
        LOADING,
        LOADED
    }
}