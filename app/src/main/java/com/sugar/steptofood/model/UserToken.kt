package com.sugar.steptofood.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserToken(
        @SerializedName("token")
        @Expose
        val token: String,

        @SerializedName("id")
        @Expose
        val userId: Int
)