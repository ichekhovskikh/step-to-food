package com.sugar.steptofood.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.extension.uploadSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FoodPresenter(view: FoodView,
                    api: ApiService) : BasePresenter<FoodView>(view, api) {

    fun getFoodImage(foodId: Int, onSuccess: (image: Bitmap) -> Unit) {
        view.onShowLoading()
        api.getFoodImage(foodId)
                .customSubscribe({
                    view.onHideLoading()
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
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
        uploadFoodPhotoAndContent(food, onSuccess)
    }

    fun removeFood(foodId: Int) {
        api.removeFood(foodId).customSubscribe({}, defaultError())
    }

    fun setLikeFood(foodId: Int, hasLike: Boolean) {
        api.setLike(foodId, hasLike).customSubscribe({}, defaultError())
    }

    private fun uploadFoodPhotoAndContent(food: Food, onSuccess: () -> Unit) {
        val file = File(food.image)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        api.uploadFile(body).uploadSubscribe({
            food.image = it
            uploadFoodContent(food, onSuccess)
        }, defaultError())
    }

    private fun uploadFoodContent(food: Food, onSuccess: () -> Unit) {
        api.addFood(food)
                .customSubscribe({
                    view.onHideLoading()
                    onSuccess.invoke()
                }, defaultError())
    }

    fun getFoodAuthorAvatar(userId: Int, onSuccess: (bitmap: Bitmap) -> Unit) {
        view.onShowLoading()
        api.getUserAvatar(userId)
                .customSubscribe({
                    val bitmap = BitmapFactory.decodeStream(it.byteStream())
                    onSuccess.invoke(bitmap)
                    view.onHideLoading()
                }, defaultError())
    }
}