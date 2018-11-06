package com.sugar.steptofood.extension

import com.sugar.steptofood.rest.response.BaseResponse
import com.sugar.steptofood.rest.response.UploadResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

fun <T> Single<BaseResponse<T>>.customSubscribe(onSuccess: (T) -> Unit,
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

fun <T> Single<Response<T>>.downloadSubscribe(onSuccess: (T) -> Unit,
                                            onError: (String) -> Unit = {}) {
    this.compose(applySingleSchedulers())
            .subscribe({ response ->
                if (response.isSuccessful && response.body() != null) {
                    onSuccess.invoke(response.body()!!)
                } else if (!response.isSuccessful && response.errorBody() != null) {
                    onError.invoke(response.errorBody()!!.string())
                }
            }, {
                onError.invoke(it.message ?: "Unexpected error")
            })
}

fun Single<UploadResponse>.uploadSubscribe(onSuccess: (String) -> Unit,
                                                onError: (String) -> Unit = {}) {
    this.compose(applySingleSchedulers())
            .subscribe({ response ->
                if (response.success) {
                    onSuccess.invoke(response.link)
                } else if (!response.success) {
                    onError.invoke(response.message)
                }
            }, {
                onError.invoke(it.message ?: "Unexpected error")
            })
}

fun <T> applySingleSchedulers() = { single: Single<T> ->
    single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}