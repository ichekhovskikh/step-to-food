package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import com.sugar.steptofood.*
import com.sugar.steptofood.R
import com.sugar.steptofood.model.*
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.paging.PagedRecipeListFactory
import com.sugar.steptofood.repository.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import javax.inject.Inject

class RecipeViewModel(private val app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var appDatabase: AppDatabase
        protected set

    @Inject
    lateinit var api: ApiService
        protected set

    @Inject
    lateinit var session: Session
        protected set

    private val recipeRepository by lazy { RecipeRepository(api, appDatabase, session, getApplication()) }
    private val pagedRecipeFactory by lazy { PagedRecipeListFactory(api, appDatabase, session) }

    init {
        App.appComponent.inject(this)
    }

    fun getComposedPagedList(products: List<Product>) =
            pagedRecipeFactory.getComposedPagedList(products)

    fun getCachePagedList(type: RecipeType, userId: Int) =
            pagedRecipeFactory.getCachePagedList(type, userId)

    fun getNetworkPagedList(type: RecipeType, userId: Int, searchName: String) =
            if (searchName == "") pagedRecipeFactory.getNetworkUserPagedList(type, userId)
            else pagedRecipeFactory.getSearchPagedList(type, userId, searchName)

    fun getRecipe(recipeId: Int): LiveData<Recipe> {
        if (!isNetworkAvailable(getApplication()))
            recipeRepository.error().postValue(app.getString(R.string.error_no_internet))
        return recipeRepository.getRecipe(recipeId)
    }

    fun removeRecipe(recipeId: Int,  onSuccess: () -> Unit = {}) {
        recipeRepository.removeRecipe(recipeId, onSuccess)
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        recipeRepository.setLikeRecipe(recipeId, hasLike)
    }

    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit) {
        recipeRepository.addRecipe(recipe, onSuccess)
    }

    fun clearCache() {
        appDatabase.businessObject.removeAll()
    }

    fun getLoadingStatus(): LiveData<BaseRepository.LoadingStatus> {
        return recipeRepository.loadingStatus()
    }

    fun getErrorMessage(): LiveData<String> {
        return recipeRepository.error()
    }
}