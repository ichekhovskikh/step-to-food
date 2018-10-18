package com.sugar.steptofood.rest

import com.sugar.steptofood.rest.response.BaseResponse
import com.sugar.steptofood.rest.response.UploadResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/register")
    fun register(@Field("name") name: String,
                 @Field("login") login: String,
                 @Field("password") password: String): Single<BaseResponse<Int>>

    @Multipart
    @POST("https://file.io")
    fun uploadFile(@Part file: MultipartBody.Part): Single<UploadResponse>

    @GET
    fun downloadFile(@Url link: String): Single<ResponseBody>
}