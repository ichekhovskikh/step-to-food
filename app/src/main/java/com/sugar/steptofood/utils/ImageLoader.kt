package com.sugar.steptofood.utils

import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.sugar.steptofood.App

fun loadImage(imageLink: String): RequestCreator? =
        Picasso.get().load(App.BASE_URL + "/" + imageLink)

fun loadAvatar(userId: Int): RequestCreator? =
        Picasso.get().load(App.BASE_URL + "/user/get/avatar?userId=" + userId)