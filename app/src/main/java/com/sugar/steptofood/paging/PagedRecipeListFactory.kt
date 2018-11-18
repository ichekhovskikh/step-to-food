package com.sugar.steptofood.paging

import android.arch.paging.*
import com.sugar.steptofood.Session
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.*
import com.sugar.steptofood.paging.source.*
import com.sugar.steptofood.utils.RecipeType

class PagedRecipeListFactory(private val api: ApiService,
                             private val appDatabase: AppDatabase,
                             private val session: Session) {
    private val config: PagedList.Config

    init {
        config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(PAGE_SIZE)
                .build()
    }

    fun getComposedPagedList(products: List<Product>): RecipePagedListWithLoadState {
        val composedSourceFactory = ComposedRecipeSourceFactory(api, products)
        return buildPagedList(composedSourceFactory)
    }

    fun getCachePagedList(type: RecipeType, userId: Int): RecipePagedListWithLoadState {
        val cacheSourceFactory = CacheUserRecipeSourceFactory(appDatabase, session, type, userId)
        return buildPagedList(cacheSourceFactory)
    }

    fun getNetworkUserPagedList(type: RecipeType, userId: Int): RecipePagedListWithLoadState {
        val networkSourceFactory = NetworkUserRecipeSourceFactory(api, appDatabase, session, type, userId)
        return buildPagedList(networkSourceFactory)
    }

    fun getSearchPagedList(type: RecipeType, userId: Int, searchName: String): RecipePagedListWithLoadState {
        val searchSourceFactory = SearchRecipeSourceFactory(api, type, userId, searchName)
        return buildPagedList(searchSourceFactory)
    }

    private fun buildPagedList(sourceFactory: StateSourceFactory<Int, Recipe>): RecipePagedListWithLoadState {
        val pagedList = LivePagedListBuilder(sourceFactory, config)
                .build()

        return RecipePagedListWithLoadState(pagedList,
                sourceFactory.getInitialLoadState(),
                sourceFactory.getAdditionalLoadState())
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}