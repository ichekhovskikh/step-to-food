package com.sugar.steptofood.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.graphics.Bitmap
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import android.graphics.BitmapFactory
import android.net.Uri
import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.downloadSubscribe
import com.sugar.steptofood.extension.readBytes
import okhttp3.*

class UserRepository(private val api: ApiService,
                     private val session: Session,
                     private val context: Context) : BaseRepository() {

    fun register(name: String, login: String, password: String, onSuccess: () -> Unit) {
        liveStatus.postValue(LoadingStatus.LOADING)
        api.register(name, login, password)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    login(login, password, onSuccess)
                }, onError())
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        liveStatus.postValue(LoadingStatus.LOADING)
        api.login(login, password)
                .customSubscribe({ response ->
                    session.token = response.token
                    session.userId = response.id!!
                    liveStatus.postValue(LoadingStatus.LOADED)
                    onSuccess.invoke()
                }, onError())
    }

    fun login(onSuccess: () -> Unit) {
        liveStatus.postValue(LoadingStatus.LOADING)
        if (session.token.isEmpty()) {
            liveStatus.postValue(LoadingStatus.LOADED)
            errorMessage.postValue("Token is absent")
        }

        api.login(session.token)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    onSuccess.invoke()
                }, onError())
    }

    fun getAvatar(userId: Int): LiveData<Bitmap?> {
        val avatar = MutableLiveData<Bitmap?>()
        api.getUserAvatar(userId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    avatar.postValue(bitmap)
                }, onError())
        return avatar
    }

    fun getUserName(userId: Int): LiveData<String> {
        liveStatus.postValue(LoadingStatus.LOADING)
        val liveName = MutableLiveData<String>()
        api.getUser(userId)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
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
        api.setUserAvatar(body).customSubscribe({}, { errorMessage.postValue(it) })
    }

    fun terminate() {
        liveStatus.postValue(LoadingStatus.LOADING)
        api.terminate()
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    session.reset()
                }, onError())
    }
}