package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import android.graphics.BitmapFactory
import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.uploadSubscribe
import com.sugar.steptofood.ui.view.UserView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserPresenter(view: UserView,
                    api: ApiService,
                    val session: Session) : BasePresenter<UserView>(view, api) {

    fun getUserAvatar(userId: Int) {
        view.onShowLoading()
        api.getUserAvatar(userId)
                .customSubscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    view.onHideLoading()
                    view.setUserAvatar(bitmap)
                }, defaultError())
    }

    fun getUserName(userId: Int) {
        view.onShowLoading()
        api.getUser(userId)
                .customSubscribe({
                    view.onHideLoading()
                    view.setUserName(it.name!!)
                }, defaultError())
    }

    fun setAvatar(uri: String) {
        val file = File(uri)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        api.uploadFile(body).uploadSubscribe({ link ->
            api.setUserAvatar(link).customSubscribe({}, defaultError())
        }, defaultError())
    }

    fun terminate() {
        view.onShowLoading()
        api.terminate()
                .customSubscribe({
                    view.onHideLoading()
                    session.reset()
                }, defaultError())
    }
}