package com.sugar.steptofood.paging

import android.arch.lifecycle.*
import android.arch.paging.*
import com.sugar.steptofood.Session
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.*
import com.sugar.steptofood.paging.source.ComposedRecipeSourceFactory
import com.sugar.steptofood.paging.source.SearchRecipeSourceFactory
import com.sugar.steptofood.utils.RecipeType

class PagedRecipeListFactory(private val api: ApiService,
                             private val appDatabase: AppDatabase,
                             private val session: Session) {
    private val config: PagedList.Config

    init {
        config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()
    }

    fun getPagedList(products: List<Product>): LiveData<PagedList<Recipe>> {
        val sourceFactory = ComposedRecipeSourceFactory(api, products)
        return LivePagedListBuilder(sourceFactory, config)
                .build()
    }

    fun getPagedList(type: RecipeType, userId: Int): LiveData<PagedList<Recipe>> {
        val sourceFactory = appDatabase.businessObject.getRangeRecipe(type, userId)
        val networkSwapCallback = RecipeNetworkSwapCallback(api, appDatabase, session, type, userId, PAGE_SIZE)
        return LivePagedListBuilder(sourceFactory, config)
                .setBoundaryCallback(networkSwapCallback)
                .build()
    }

    fun getPagedList(type: RecipeType, userId: Int, searchName: String): LiveData<PagedList<Recipe>> {
        val sourceFactory = SearchRecipeSourceFactory(api, type, userId, searchName)
        return LivePagedListBuilder(sourceFactory, config)
                .build()
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}