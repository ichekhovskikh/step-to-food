package com.sugar.steptofood.presenter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.extension.downloadSubscribe
import com.sugar.steptofood.extension.readBytes
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FoodPresenter(view: FoodView,
                    api: ApiService,
                    private val context: Context) : BasePresenter<FoodView>(view, api) {

    fun getFoodImage(foodId: Int, onSuccess: (image: Bitmap?) -> Unit) {
        view.onShowLoading()
        api.getFoodImage(foodId)
                .downloadSubscribe({
                    view.onHideLoading()
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    onSuccess.invoke(bitmap)
                }, defaultError())
    }

    fun getFood(foodId: Int, onSuccess: (food: Food) -> Unit) {
        view.onShowLoading()
        api.getFood(foodId)
                .customSubscribe({
                    view.onHideLoading()
                    onSuccess.invoke(it)
                }, defaultError())
    }

    fun addFood(food: Food, onSuccess: () -> Unit) {
        view.onShowLoading()
        uploadContentAndFoodPhoto(food, onSuccess)
    }

    fun removeFood(foodId: Int) {
        api.removeFood(foodId).customSubscribe({}, defaultError())
    }

    fun setLikeFood(foodId: Int, hasLike: Boolean) {
        api.setLike(foodId, hasLike).customSubscribe({}, defaultError())
    }

    private fun uploadContentAndFoodPhoto(food: Food, onSuccess: () -> Unit) {
        val uri = Uri.parse(food.image)
        food.image = null
        api.addFood(food)
                .customSubscribe({addedFoodId ->
                    uploadFoodImage(addedFoodId, uri, onSuccess)
                }, defaultError())
    }

    private fun uploadFoodImage(foodId: Int, uri: Uri, onSuccess: () -> Unit) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream!!.readBytes()
        inputStream.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val body = MultipartBody.Part.createFormData("image", uri.lastPathSegment, requestFile)

        api.setFoodImage(foodId, body)
                .customSubscribe({
                    view.onHideLoading()
                    onSuccess.invoke()
                }, defaultError())
    }

    fun getFoodAuthorAvatar(userId: Int, onSuccess: (bitmap: Bitmap?) -> Unit) {
        view.onShowLoading()
        api.getUserAvatar(userId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    onSuccess.invoke(bitmap)
                    view.onHideLoading()
                }, defaultError())
    }
}