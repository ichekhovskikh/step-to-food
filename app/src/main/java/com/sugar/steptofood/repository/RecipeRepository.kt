package com.sugar.steptofood.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.Uri
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.extension.readBytes
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RecipeRepository(private val api: ApiService,
                       private val appDatabase: AppDatabase,
                       private val session: Session,
                       private val context: Context) : BaseRepository() {

    fun getRecipe(recipeId: Int): LiveData<Recipe> {
        liveStatus.postValue(LoadingStatus.LOADING)
        val liveRecipe = MutableLiveData<Recipe>()
        liveRecipe.value = appDatabase.recipeDao().getById(recipeId)
        api.getRecipe(recipeId)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    liveRecipe.postValue(it)
                }, onError())
        return liveRecipe
    }

    fun removeRecipe(recipeId: Int, onSuccess: () -> Unit = {}) {
        api.removeRecipe(recipeId).customSubscribe({
            appDatabase.recipeDao().remove(recipeId)
            onSuccess.invoke()
        }, onError())
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        api.setLike(recipeId, hasLike).customSubscribe({
            appDatabase.businessObject.setOrRemoveLike(recipeId, session.userId, hasLike)
        }, onError())
    }

    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit = {}) {
        liveStatus.postValue(LoadingStatus.LOADING)
        uploadContentAndRecipePhoto(recipe, onSuccess)
    }

    private fun uploadContentAndRecipePhoto(recipe: Recipe, onSuccess: () -> Unit) {
        val uri = Uri.parse(recipe.image)
        recipe.image = null
        api.addRecipe(recipe)
                .customSubscribe({ addedRecipeId ->
                    uploadRecipeImage(addedRecipeId, uri, onSuccess)
                }, onError())
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
                }, onError())
    }
}