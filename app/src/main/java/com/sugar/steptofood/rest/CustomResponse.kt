package com.sugar.steptofood.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomResponse<T>(
        @SerializedName("success")
        @Expose
        val success: Boolean,

        @SerializedName("content")
        @Expose
        val content: T?,

        @SerializedName("error")
        @Expose
        val error: String?
) : Serializable