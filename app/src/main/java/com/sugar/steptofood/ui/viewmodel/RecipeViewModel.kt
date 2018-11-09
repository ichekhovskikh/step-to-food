package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.paging.PagedRecipeRepository
import com.sugar.steptofood.repository.RecipeRepository
import com.sugar.steptofood.rest.ApiService
import javax.inject.Inject

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    private lateinit var dbHelper: SQLiteHelper

    @Inject
    private lateinit var api: ApiService

    @Inject
    private lateinit var session: Session

    private val recipeRepository by lazy { RecipeRepository(api, application) }
    private lateinit var pagedRecipeRepository: PagedRecipeRepository
}