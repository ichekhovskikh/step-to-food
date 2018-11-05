package com.sugar.steptofood.rest

import com.sugar.steptofood.model.*
import com.sugar.steptofood.rest.response.*
import com.sugar.steptofood.utils.FoodType
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
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

    @GET("/user")
    fun getUser(@Query("userId") userId: Int): Single<BaseResponse<User>>

    @GET("/user/get/avatar")
    fun getUserAvatar(@Query("userId") userId: Int): Single<Response<ResponseBody>>

    @Multipart
    @POST("/user/set/avatar")
    fun setUserAvatar(@Part file: MultipartBody.Part): Single<BaseResponse<Int>>

    @GET("/food")
    fun getFood(@Query("foodId") foodId: Int): Single<BaseResponse<Food>>

    @GET("/food/search")
    fun searchFoods(@Query("userId") userId: Int,
                    @Query("searchName") searchName: String,
                    @Query("foodType") type: FoodType,
                    @Query("start") start: Int,
                    @Query("size") size: Int): Single<BaseResponse<List<Food>>>

    @GET("/food/get/image")
    fun getFoodImage(@Query("foodId") foodId: Int): Single<Response<ResponseBody>>

    @Multipart
    @POST("/food/set/image")
    fun setFoodImage(@Query("foodId") foodId: Int,
                     @Part file: MultipartBody.Part): Single<BaseResponse<Int>>

    @GET("/product")
    fun getProduct(@Query("productId") productId: Int): Single<BaseResponse<Product>>

    @GET("/product/all")
    fun getAllProducts(): Single<BaseResponse<List<Product>>>

    @GET("/product/search/food")
    fun getProductsByFood(@Query("search") search: Int): Single<BaseResponse<List<Product>>>

    @GET("/product/search/name")
    fun searchProducts(@Query("search") search: String): Single<BaseResponse<List<Product>>>

    @POST("/food/search/products")
    fun searchFoodsByProducts(@Body products: List<Int>,
                              @Query("start") start: Int,
                              @Query("size") size: Int): Single<BaseResponse<List<Food>>>

    @POST("/food/add")
    fun addFood(@Body food: Food): Single<BaseResponse<Int>>

    @GET("/food/remove")
    fun removeFood(@Query("foodId") foodId: Int): Single<BaseResponse<Int>>

    @GET("/food/like")
    fun setLike(@Query("foodId") foodId: Int,
                @Query("hasLike") hasLike: Boolean): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/food/update")
    fun updateFood(@Field("food") food: Food): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/user/update/name")
    fun updateUserName(@Field("name") name: String): Single<BaseResponse<Int>>
}