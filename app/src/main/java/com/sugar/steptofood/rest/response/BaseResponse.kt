package com.sugar.steptofood.rest.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BaseResponse<T>(
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