package com.sugar.steptofood.ui.view

import android.graphics.Bitmap

interface UserView : BaseView {
    fun setUserName(name: String)
    fun setUserAvatar(image: Bitmap)
}