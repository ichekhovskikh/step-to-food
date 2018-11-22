package com.sugar.steptofood.repository

import android.arch.lifecycle.*
import android.content.Context
import android.net.Uri
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.utils.extension.*
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import okhttp3.*
import java.util.concurrent.Executor

class RecipeRepository(private val api: ApiService,
                       private val appDatabase: AppDatabase,
                       private val ioExecutor: Executor,
                       private val session: Session,
                       private val context: Context) : BaseStatusRepository() {

    fun getRecipe(recipeId: Int): LiveData<FullRecipeInfo> {
        status.postValue(NetworkState.LOADING)
        val liveRecipe = MutableLiveData<FullRecipeInfo>()
        getRecipeFromCacheAtLoadTime(liveRecipe, recipeId)
        api.getRecipe(recipeId)
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    liveRecipe.postValue(it)
                }, onError())
        return liveRecipe
    }

    fun removeRecipe(recipeId: Int, onSuccess: () -> Unit = {}) {
        api.removeRecipe(recipeId).smartSubscribe({
            ioExecutor.execute { appDatabase.recipeDao().remove(recipeId) }
            onSuccess.invoke()
        }, onError())
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        api.setLike(recipeId, hasLike).smartSubscribe({
            setLikeRecipeInCache(recipeId, hasLike)
        }, onError())
    }

    fun getRecipesFromCache(type: RecipeType, searchName: String, userId: Int, start: Int, size: Int) =
                appDatabase.businessLogicObject.getRecipes(type, searchName, userId, session.userId, start, size)

    fun addRecipesInCache(recipes: List<FullRecipeInfo>, type: RecipeType, userId: Int) {
        ioExecutor.execute {
            appDatabase.runInTransaction {
                appDatabase.businessLogicObject.smartAddAllRecipes(recipes, type, userId, session.userId)
            }
        }
    }

    fun refreshAllRecipesInCache(recipes: List<FullRecipeInfo>, type: RecipeType, userId: Int) {
        ioExecutor.execute {
            appDatabase.runInTransaction {
                appDatabase.businessLogicObject.removeAll()
                appDatabase.businessLogicObject.smartAddAllRecipes(recipes, type, userId, session.userId)
            }
        }
    }

    fun addRecipe(recipe: FullRecipeInfo, onSuccess: () -> Unit = {}) {
        status.postValue(NetworkState.LOADING)
        uploadContentAndRecipePhoto(recipe, onSuccess)
    }

    private fun uploadContentAndRecipePhoto(recipe: FullRecipeInfo, onSuccess: () -> Unit) {
        val uri = Uri.parse(recipe.image)
        recipe.image = null
        api.addRecipe(recipe)
                .smartSubscribe({ addedRecipeId ->
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
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    onSuccess.invoke()
                }, onError())
    }

    private fun getRecipeFromCacheAtLoadTime(liveRecipe: MutableLiveData<FullRecipeInfo>, recipeId: Int) {
        ioExecutor.execute {
            liveRecipe.postValue(appDatabase.recipeDao().getById(recipeId))
        }
    }

    private fun setLikeRecipeInCache(recipeId: Int, hasLike: Boolean) {
        ioExecutor.execute {
            appDatabase.runInTransaction {
                appDatabase.businessLogicObject.setLike(recipeId, session.userId, hasLike)
            }
        }
    }
}