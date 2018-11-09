package com.sugar.steptofood.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.extension.downloadSubscribe
import com.sugar.steptofood.extension.readBytes
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.RecipeView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RecipePresenter(view: RecipeView,
                      api: ApiService,
                      private val context: Context) : BasePresenter<RecipeView>(view, api) {

    fun getRecipeImage(recipeId: Int): LiveData<Bitmap?> {
        val image = MutableLiveData<Bitmap?>()
        api.getRecipeImage(recipeId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    image.postValue(bitmap)
                }, defaultError())
        return image
    }

    fun getRecipe(recipeId: Int): LiveData<Recipe> {
        view.onShowLoading()
        val liveRecipe = MutableLiveData<Recipe>()
        api.getRecipe(recipeId)
                .customSubscribe({
                    view.onHideLoading()
                    liveRecipe.postValue(it)
                }, defaultError())
        return liveRecipe
    }

    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit) {
        view.onShowLoading()
        uploadContentAndRecipePhoto(recipe, onSuccess)
    }

    fun removeRecipe(recipeId: Int) {
        api.removeRecipe(recipeId).customSubscribe({}, defaultError())
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        api.setLike(recipeId, hasLike).customSubscribe({}, defaultError())
    }

    private fun uploadContentAndRecipePhoto(recipe: Recipe, onSuccess: () -> Unit) {
        val uri = Uri.parse(recipe.image)
        recipe.image = null
        api.addRecipe(recipe)
                .customSubscribe({addedRecipeId ->
                    uploadRecipeImage(addedRecipeId, uri, onSuccess)
                }, defaultError())
    }

    private fun uploadRecipeImage(recipeId: Int, uri: Uri, onSuccess: () -> Unit) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream!!.readBytes()
        inputStream.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val body = MultipartBody.Part.createFormData("image", uri.lastPathSegment, requestFile)

        api.setRecipeImage(recipeId, body)
                .customSubscribe({
                    view.onHideLoading()
                    onSuccess.invoke()
                }, defaultError())
    }

    fun getRecipeAuthorAvatar(userId: Int): LiveData<Bitmap?> {
        val avatar = MutableLiveData<Bitmap?>()
        api.getUserAvatar(userId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    avatar.postValue(bitmap)
                }, defaultError())
        return avatar
    }
}