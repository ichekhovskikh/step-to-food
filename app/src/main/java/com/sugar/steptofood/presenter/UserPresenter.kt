package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import android.graphics.BitmapFactory
import com.sugar.steptofood.ui.view.UserView

class UserPresenter(view: UserView,
                    api: ApiService) : BasePresenter<UserView>(view, api) {

    fun getUserAvatar(userId: Int) {
        //view.onShowLoading()
        api.getUserAvatar(userId)
                .customSubscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    //view.onHideLoading()
                    view.setUserAvatar(bitmap)
                }, defaultError())
    }

    fun getUserName(userId: Int) {
        //view.onShowLoading()
        api.getUser(userId)
                .customSubscribe({
                    //view.onHideLoading()
                    view.setUserName(it.name!!)
                }, defaultError())
    }
}