package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import com.sugar.steptofood.*
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.paging.PagedRecipeListFactory
import com.sugar.steptofood.repository.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.*
import java.util.concurrent.Executor
import javax.inject.Inject

class RecipeViewModel(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var appDatabase: AppDatabase
        protected set

    @Inject
    lateinit var api: ApiService
        protected set

    @Inject
    lateinit var session: Session
        protected set

    @Inject
    lateinit var ioExecutor: Executor
        protected set

    private val recipeRepository by lazy { RecipeRepository(api, appDatabase, ioExecutor, session, getApplication()) }
    private val pagedRecipeListFactory by lazy { PagedRecipeListFactory(api) }

    init {
        App.appComponent.inject(this)
    }

    fun getComposedRecipePagedList(products: List<FullProductInfo>) =
            pagedRecipeListFactory.getComposedRecipePagedList(products)

    fun getCacheTypeRecipePagedList(type: RecipeType, userId: Int, searchName: String) =
            pagedRecipeListFactory.getCacheTypeRecipePagedList { start, size, callback ->
                callback.invoke(recipeRepository.getRecipesFromCache(type, searchName, userId, start, size))
            }

    fun getNetworkTypeRecipePagedList(type: RecipeType, userId: Int, searchName: String?) =
            if (searchName == null || searchName == "") {
                pagedRecipeListFactory.getNetworkTypeRecipePagedList(
                        type = type,
                        userId = userId,
                        handleInitialLoadResponse = {
                            recipeRepository.addRecipesInCache(it, type, userId)
                        },
                        handleAdditionalLoadResponse = {
                            recipeRepository.refreshAllRecipesInCache(it, type, userId)
                        })
            } else pagedRecipeListFactory.getSearchRecipePagedList(type, userId, searchName)

    fun getRecipe(recipeId: Int): LiveData<FullRecipeInfo> {
        return recipeRepository.getRecipe(recipeId)
    }

    fun removeRecipe(recipeId: Int, onSuccess: () -> Unit = {}) {
        recipeRepository.removeRecipe(recipeId, onSuccess)
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        recipeRepository.setLikeRecipe(recipeId, hasLike)
    }

    fun addRecipe(recipe: FullRecipeInfo, onSuccess: () -> Unit) {
        recipeRepository.addRecipe(recipe, onSuccess)
    }

    fun getLoadingStatus(): LiveData<NetworkState> {
        return recipeRepository.loadingStatus()
    }
}