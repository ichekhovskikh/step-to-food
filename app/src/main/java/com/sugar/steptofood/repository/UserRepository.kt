package com.sugar.steptofood.repository

import android.arch.lifecycle.*
import android.content.Context
import com.sugar.steptofood.utils.extension.*
import com.sugar.steptofood.rest.ApiService
import android.net.Uri
import com.sugar.steptofood.Session
import com.sugar.steptofood.utils.NetworkState
import okhttp3.*

class UserRepository(private val api: ApiService,
                     private val session: Session,
                     private val context: Context) : BaseStatusRepository() {

    fun register(name: String, login: String, password: String, onSuccess: () -> Unit) {
        status.postValue(NetworkState.LOADING)
        api.register(name, login, password)
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    login(login, password, onSuccess)
                }, onError())
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        status.postValue(NetworkState.LOADING)
        api.login(login, password)
                .smartSubscribe({ response ->
                    session.token = response.token
                    session.userId = response.id!!
                    status.postValue(NetworkState.LOADED)
                    onSuccess.invoke()
                }, onError())
    }

    fun login(onSuccess: () -> Unit) {
        status.postValue(NetworkState.LOADING)
        if (session.token.isEmpty()) {
            onError().invoke("Token is absent")
        }

        api.login(session.token)
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    onSuccess.invoke()
                }, onError())
    }

    fun getUserName(userId: Int): LiveData<String> {
        status.postValue(NetworkState.LOADING)
        val liveName = MutableLiveData<String>()
        api.getUser(userId)
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    liveName.postValue(it.name)
                }, onError())
        return liveName
    }

    fun setAvatar(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream!!.readBytes()
        inputStream.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val body = MultipartBody.Part.createFormData("image", uri.lastPathSegment, requestFile)
        api.setUserAvatar(body).smartSubscribe({}, onError())
    }

    fun terminate() {
        status.postValue(NetworkState.LOADING)
        api.terminate()
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    session.reset()
                }, onError())
    }
}