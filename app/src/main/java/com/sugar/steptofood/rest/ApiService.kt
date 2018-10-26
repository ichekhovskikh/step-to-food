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
    @POST("/register")
    fun register(@Field("name") name: String,
                 @Field("login") login: String,
                 @Field("password") password: String): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("login") login: String,
              @Field("password") password: String): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("token") token: String): Single<BaseResponse<User>>

    @FormUrlEncoded
    @POST("/changePassword")
    fun changePassword(@Field("password") password: String,
                       @Field("new_password") newPassword: String): Single<BaseResponse<Int>>

    @GET("/terminate")
    fun terminate(): Single<BaseResponse<Int>>

    @GET("/user?userId={userId}")
    fun getUser(@Path("userId") userId: Int): Single<BaseResponse<User>>

    @GET("/user_avatar?userId={userId}")
    fun getUserAvatar(@Path("userId") userId: Int): Single<BaseResponse<ResponseBody>>

    @GET("/food?foodId={foodId}")
    fun getFood(@Path("foodId") foodId: Int): Single<BaseResponse<Food>>

    @GET("/foods?userId={userId}&type={type}&start={start}&size={size}")
    fun getFoodAll(@Path("userId") userId: Int,
                   @Path("type") type: FoodType,
                   @Path("start") start: Int,
                   @Path("size") size: Int): Single<BaseResponse<List<Food>>>

    @GET("/user_avatar?userId={foodId}")
    fun getFoodImage(@Path("foodId") foodId: Int): Single<BaseResponse<ResponseBody>>

    @GET("/product?productId={productId}")
    fun getProduct(@Path("productId") productId: Int): Single<BaseResponse<Product>>

    @GET("/allProducts")
    fun getAllProducts(): Single<BaseResponse<List<Product>>>

    @GET("/productsByFood?foodId={foodId}")
    fun getProductsByFood(@Path("foodId") foodId: Int): Single<BaseResponse<List<Product>>>

    @GET("/searchFoods?search={name}")
    fun searchFoods(@Path("name") name: String): Single<BaseResponse<List<Food>>>

    @GET("/searchProducts?search={name}")
    fun searchProducts(@Path("name") name: String): Single<BaseResponse<List<Product>>>

    @FormUrlEncoded
    @POST("/searchFoodsByProduct")
    fun searchFoodsByProduct(@Field("products") products: List<Product>): Single<BaseResponse<List<Food>>>

    @FormUrlEncoded
    @POST("/addFood")
    fun addFood(@Field("food") food: Food): Single<BaseResponse<Int>>

    @GET("/removeFood?foodId={foodId}")
    fun removeFood(@Path("foodId") foodId: Int): Single<BaseResponse<Int>>

    @GET("/setLike?foodId={foodId}&like={hasLike}")
    fun setLike(@Path("foodId") foodId: Int,
                @Path("hasLike") hasLike: Boolean): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/updateFood")
    fun updateFood(@Field("food") food: Food): Single<BaseResponse<Int>>

    @FormUrlEncoded
    @POST("/updateUser")
    fun updateUser(@Field("user") user: User): Single<BaseResponse<Int>>

    @Multipart
    @POST("https://file.io")
    fun uploadFile(@Part file: MultipartBody.Part): Single<UploadResponse>

    @GET
    fun downloadFile(@Url link: String): Single<ResponseBody>
}