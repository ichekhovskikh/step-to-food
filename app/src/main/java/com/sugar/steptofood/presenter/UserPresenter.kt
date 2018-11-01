package com.sugar.steptofood.presenter

import android.content.Context
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import android.graphics.BitmapFactory
import android.net.Uri
import com.sugar.steptofood.App
import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.downloadSubscribe
import com.sugar.steptofood.ui.view.UserView
import com.sugar.steptofood.utils.readBytes
import okhttp3.*
import java.io.File

class UserPresenter(view: UserView,
                    api: ApiService,
                    val session: Session,
                    val context: Context) : BasePresenter<UserView>(view, api) {

    fun getAvatar(userId: Int) {
        view.onShowLoading()
        api.getUserAvatar(userId)
                .downloadSubscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    view.onHideLoading()
                    if (bitmap != null)
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

    fun setAvatar(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = readBytes(inputStream!!)
        inputStream.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val body = MultipartBody.Part.createFormData("image", uri.lastPathSegment, requestFile)
        api.setUserAvatar(body).customSubscribe({}, defaultError())
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