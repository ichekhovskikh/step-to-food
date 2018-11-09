package com.sugar.steptofood.repository

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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RecipeRepository(private val api: ApiService,
                       private val context: Context) : BaseRepository() {

    fun getRecipeImage(recipeId: Int): LiveData<Bitmap?> {
        val image = MutableLiveData<Bitmap?>()
        api.getRecipeImage(recipeId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    image.postValue(bitmap)
                }, { errorMessage.postValue(it) })
        return image
    }

    fun getRecipe(recipeId: Int): LiveData<Recipe> {
        liveStatus.postValue(LoadingStatus.LOADING)
        val liveRecipe = MutableLiveData<Recipe>()
        api.getRecipe(recipeId)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    liveRecipe.postValue(it)
                }, { errorMessage.postValue(it) })
        return liveRecipe
    }

    fun removeRecipe(recipeId: Int) {
        api.removeRecipe(recipeId).customSubscribe({}, { errorMessage.postValue(it) })
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        api.setLike(recipeId, hasLike).customSubscribe({}, { errorMessage.postValue(it) })
    }

    fun getRecipeAuthorAvatar(userId: Int): LiveData<Bitmap?> {
        val avatar = MutableLiveData<Bitmap?>()
        api.getUserAvatar(userId)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    avatar.postValue(bitmap)
                }, { errorMessage.postValue(it) })
        return avatar
    }

    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit) {
        liveStatus.postValue(LoadingStatus.LOADING)
        uploadContentAndRecipePhoto(recipe, onSuccess)
    }

    private fun uploadContentAndRecipePhoto(recipe: Recipe, onSuccess: () -> Unit) {
        val uri = Uri.parse(recipe.image)
        recipe.image = null
        api.addRecipe(recipe)
                .customSubscribe({ addedRecipeId ->
                    uploadRecipeImage(addedRecipeId, uri, onSuccess)
                }, { errorMessage.postValue(it) })
    }

    private fun uploadRecipeImage(recipeId: Int, uri: Uri, onSuccess: () -> Unit) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream!!.readBytes()
        inputStream.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val body = MultipartBody.Part.createFormData("image", uri.lastPathSegment, requestFile)

        api.setRecipeImage(recipeId, body)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    onSuccess.invoke()
                }, { errorMessage.postValue(it) })
    }
}