package com.sugar.steptofood.extension

import com.sugar.steptofood.rest.response.BaseResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<BaseResponse<T>>.customSubscribe(onSuccess: (T) -> Unit,
                                                onError: (String) -> Unit) {
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

fun <T> applySingleSchedulers() = { single: Single<T> ->
    single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}