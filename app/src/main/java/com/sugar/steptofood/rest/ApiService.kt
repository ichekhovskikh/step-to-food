package com.sugar.steptofood.rest

import com.sugar.steptofood.model.*
import com.sugar.steptofood.utils.RecipeType
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
                 @Field("password") password: String): Single<CustomResponse<Int>>

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("login") login: String,
              @Field("password") password: String): Single<CustomResponse<User>>

    @FormUrlEncoded
    @POST("/user/check")
    fun login(@Field("token") token: String): Single<CustomResponse<User>>

    @FormUrlEncoded
    @POST("/user/update/password")
    fun changePassword(@Field("password") password: String,
                       @Field("newPassword") newPassword: String): Single<CustomResponse<Int>>

    @GET("/user/terminate")
    fun terminate(): Single<CustomResponse<Int>>

    @GET("/user")
    fun getUser(@Query("userId") userId: Int): Single<CustomResponse<User>>

    @GET("/user/get/avatar")
    fun getUserAvatar(@Query("userId") userId: Int): Single<Response<ResponseBody>>

    @Multipart
    @POST("/user/set/avatar")
    fun setUserAvatar(@Part file: MultipartBody.Part): Single<CustomResponse<Int>>

    @GET("/food")
    fun getRecipe(@Query("foodId") recipeId: Int): Single<CustomResponse<Recipe>>

    @GET("/food/search")
    fun searchRecipes(@Query("userId") userId: Int,
                      @Query("searchName") searchName: String,
                      @Query("foodType") type: RecipeType,
                      @Query("start") start: Int,
                      @Query("size") size: Int): Single<CustomResponse<List<Recipe>>>

    @GET("/food/get/image")
    fun getRecipeImage(@Query("foodId") recipeId: Int): Single<Response<ResponseBody>>

    @Multipart
    @POST("/food/set/image")
    fun setRecipeImage(@Query("foodId") recipeId: Int,
                       @Part file: MultipartBody.Part): Single<CustomResponse<Int>>

    @GET("/product")
    fun getProduct(@Query("productId") productId: Int): Single<CustomResponse<Product>>

    @GET("/product/all")
    fun getAllProducts(): Single<CustomResponse<List<Product>>>

    @GET("/product/search/food")
    fun getProductsByRecipe(@Query("search") search: Int): Single<CustomResponse<List<Product>>>

    @GET("/product/search/name")
    fun searchProducts(@Query("search") search: String): Single<CustomResponse<List<Product>>>

    @POST("/food/search/products")
    fun searchRecipesByProducts(@Body products: List<Int>,
                                @Query("start") start: Int,
                                @Query("size") size: Int): Single<CustomResponse<List<Recipe>>>

    @POST("/food/add")
    fun addRecipe(@Body recipe: Recipe): Single<CustomResponse<Int>>

    @GET("/food/remove")
    fun removeRecipe(@Query("foodId") recipeId: Int): Single<CustomResponse<Int>>

    @GET("/food/like")
    fun setLike(@Query("foodId") recipeId: Int,
                @Query("hasLike") hasLike: Boolean): Single<CustomResponse<Int>>

    @FormUrlEncoded
    @POST("/food/update")
    fun updateRecipe(@Field("recipe") recipe: Recipe): Single<CustomResponse<Int>>

    @FormUrlEncoded
    @POST("/user/update/name")
    fun updateUserName(@Field("name") name: String): Single<CustomResponse<Int>>
}