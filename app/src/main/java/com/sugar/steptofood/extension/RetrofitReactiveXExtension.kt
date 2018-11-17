package com.sugar.steptofood.extension

import com.sugar.steptofood.rest.CustomResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

fun <T> Single<CustomResponse<T>>.customSubscribe(onSuccess: (T) -> Unit,
                                                  onError: (String) -> Unit = {}) {
    this.compose(applySingleSchedulers())
            .subscribe({ response ->
                if (response.success && response.content != null) {
                    onSuccess.invoke(response.content)
                } else if (!response.success && response.error != null) {
                    onError.invoke(response.error)
                }
            }, {
                onError.invoke(it.message ?: "Unexpected error")
            })
}

fun <T> Single<T>.querySubscribe(onSuccess: (T) -> Unit,
                                 onError: (String) -> Unit = {}) {
    this.compose(applySingleSchedulers())
            .subscribe({ data ->
                onSuccess.invoke(data)
            }, {
                onError.invoke(it.message ?: "Unexpected error")
            })
}

fun <T> Single<T>.querySubscribe() {
    this.compose(applySingleSchedulers())
            .subscribe()
}

fun <T> applySingleSchedulers() = { single: Single<T> ->
    single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}