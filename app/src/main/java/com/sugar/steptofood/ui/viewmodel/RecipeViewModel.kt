package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.paging.PagedRecipeRepository
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.paging.factory.ComposedRecipeSourceFactory
import com.sugar.steptofood.paging.factory.UserRecipeSourceFactory
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.repository.RecipeRepository
import com.sugar.steptofood.repository.UserRepository
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.RecipeType
import com.sugar.steptofood.utils.isNetworkAvailable
import javax.inject.Inject

class RecipeViewModel(private val app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var dbHelper: SQLiteHelper
        protected set

    @Inject
    lateinit var api: ApiService
        protected set

    @Inject
    lateinit var session: Session
        protected set

    private val recipeRepository by lazy { RecipeRepository(api, dbHelper, session, getApplication()) }
    private val userRepository by lazy { UserRepository(api, session, getApplication()) }
    private var pagedRecipeRepository: PagedRecipeRepository? = null

    init {
        App.appComponent.inject(this)
    }

    fun getRecipeSourceFactory(userId: Int, recipeType: RecipeType, search: String): BaseRecipeFactory =
            UserRecipeSourceFactory(api, session, dbHelper, userId, recipeType, search)

    fun getRecipeSourceFactory(products: List<Product>): BaseRecipeFactory =
            ComposedRecipeSourceFactory(api, products)

    fun setLivePagedRecipeList(owner: LifecycleOwner, sourceFactory: BaseRecipeFactory, adapter: BaseRecipeAdapter) {
        pagedRecipeRepository?.removeObservers(owner)
        pagedRecipeRepository = PagedRecipeRepository(sourceFactory)
        pagedRecipeRepository?.observe(owner, Observer { recipes ->
            adapter.submitList(recipes)
        })
    }

    fun refreshPagedRecipeList(sourceFactory: BaseRecipeFactory) {
        pagedRecipeRepository?.refreshData(sourceFactory)
    }

    fun getRecipeImage(recipeId: Int): LiveData<Bitmap?> {
        return recipeRepository.getRecipeImage(recipeId)
    }

    fun getRecipe(recipeId: Int): LiveData<Recipe> {
        if (!isNetworkAvailable(getApplication()))
            recipeRepository.error().postValue(app.getString(R.string.error_no_internet))
        return recipeRepository.getRecipe(recipeId)
    }

    fun removeRecipe(recipeId: Int) {
        recipeRepository.removeRecipe(recipeId) {
            pagedRecipeRepository?.refreshData()
        }
    }

    fun setLikeRecipe(recipeId: Int, hasLike: Boolean) {
        recipeRepository.setLikeRecipe(recipeId, hasLike)
    }

    fun getRecipeAuthorAvatar(userId: Int): LiveData<Bitmap?> {
        return userRepository.getAvatar(userId)
    }

    fun addRecipe(recipe: Recipe, onSuccess: () -> Unit) {
        recipeRepository.addRecipe(recipe, onSuccess)
    }

    fun clearCache() {
        dbHelper.recipeBusinessObject.removeAll()
    }

    fun getLoadingStatus(): LiveData<BaseRepository.LoadingStatus> {
        return recipeRepository.loadingStatus()
    }

    fun getErrorMessage(): LiveData<String> {
        return recipeRepository.error()
    }
}