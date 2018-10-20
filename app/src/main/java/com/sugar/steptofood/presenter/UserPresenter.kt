package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.fragment.user.UserFragment
import android.graphics.BitmapFactory

class UserPresenter(fragment: UserFragment,
                    api: ApiService) : BasePresenter<UserFragment>(fragment, api) {

    fun getUserAvatar(userId: Int) {
//        view.onShowLoading()
        api.getUserAvatar(userId)
                .customSubscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    //view.onHideLoading()
                    view.setUserAvatar(bitmap)
                }, defaultError())
    }
}