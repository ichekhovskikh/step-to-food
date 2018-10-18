package com.sugar.steptofood.rest.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UploadResponse(
        @SerializedName("success")
        @Expose
        val success: Boolean,

        @SerializedName("link")
        @Expose
        val link: String,

        @SerializedName("message")
        @Expose
        val message: String
) : Serializable