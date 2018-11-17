package com.sugar.steptofood.utils

import com.squareup.picasso.Picasso
import com.sugar.steptofood.App

fun loadImage(imageLink: String) = Picasso.get().load(App.BASE_URL + "/" + imageLink)

fun loadAvatar(userId: Int) = Picasso.get().load(App.BASE_URL + "/user/get/avatar?userId=" + userId)