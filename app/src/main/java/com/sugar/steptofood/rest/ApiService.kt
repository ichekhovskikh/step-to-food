package com.sugar.steptofood.rest

import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.response.*
import com.sugar.steptofood.utils.FoodType
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/user/register")
    fun register(@Field("name") name: String,
                 @Field("login") login: String,
                 @Field("password") password: String): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("login") login: String,
              @Field("password") password: String): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/user/check")
    fun login(@Field("token") token: String): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/user/update/password")
    fun changePassword(@Field("password") password: String,
                       @Field("newPassword") newPassword: String): Single<BaseResponse<Int>>

    @GET("/user/terminate")
    fun terminate(): Single<BaseResponse<Int>>

    @GET("/user?userId={userId}")
    fun getUser(@Path("userId") userId: Int): Single<BaseResponse<User>>

    @GET("/user/get/avatar?userId={userId}")
    fun getUserAvatar(@Path("userId") userId: Int): Single<BaseResponse<ResponseBody>>

    @GET("/user/set/avatar?uri={uri}")
    fun setUserAvatar(@Path("uri") uri: String): Single<BaseResponse<Int>>

    @GET("/food?foodId={foodId}")
    fun getFood(@Path("foodId") foodId: Int): Single<BaseResponse<Food>>

    @GET("/food/search?userId={userId}&searchName={searchName}&type={type}&start={startId}&size={size}")
    fun searchFoods(@Path("userId") userId: Int,
                    @Path("searchName") searchName: String,
                    @Path("type") type: FoodType,
                    @Path("startId") startId: Int,
                    @Path("size") size: Int): Single<BaseResponse<List<Food>>>

    @GET("/food/get/image?userId={foodId}")
    fun getFoodImage(@Path("foodId") foodId: Int): Single<BaseResponse<ResponseBody>>

    @GET("/product?productId={productId}")
    fun getProduct(@Path("productId") productId: Int): Single<BaseResponse<Product>>

    @GET("/product/all")
    fun getAllProducts(): Single<BaseResponse<List<Product>>>

    @GET("/product/search/food?search={foodId}")
    fun getProductsByFood(@Path("foodId") foodId: Int): Single<BaseResponse<List<Product>>>

    @GET("/product/search/name?search={name}")
    fun searchProducts(@Path("name") name: String): Single<BaseResponse<List<Product>>>

    @FormUrlEncoded
    @POST("/food/search/products?start={startId}&size={size}")
    fun searchFoodsByProducts(@Field("productIds") products: List<Int>,
                              @Path("startId") startId: Int,
                              @Path("size") size: Int): Single<BaseResponse<List<Food>>>

    @FormUrlEncoded
    @POST("/food/add")
    fun addFood(@Field("food") food: Food): Single<BaseResponse<Int>>

    @GET("/food/remove?foodId={foodId}")
    fun removeFood(@Path("foodId") foodId: Int): Single<BaseResponse<Int>>

    @GET("/food/like?foodId={foodId}&hasLike={hasLike}")
    fun setLike(@Path("foodId") foodId: Int,
                @Path("hasLike") hasLike: Boolean): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/food/update")
    fun updateFood(@Field("food") food: Food): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/user/update/name")
    fun updateUserName(@Field("name") name: String): Single<BaseResponse<Int>>

    @Multipart
    @POST("https://file.io")
    fun uploadFile(@Part file: MultipartBody.Part): Single<UploadResponse>

    @GET
    fun downloadFile(@Url link: String): Single<ResponseBody>
}