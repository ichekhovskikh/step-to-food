package com.sugar.steptofood.paging

import android.arch.paging.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.paging.source.*
import com.sugar.steptofood.utils.RecipeType

class PagedRecipeListFactory(private val api: ApiService) {
    private val config: PagedList.Config

    init {
        config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(PAGE_SIZE)
                .build()
    }

    fun getComposedRecipePagedList(products: List<FullProductInfo>) =
        buildPagedList(ComposedRecipeSourceFactory(api, products))

    fun getSearchRecipePagedList(type: RecipeType, userId: Int, searchName: String) =
        buildPagedList(SearchRecipeSourceFactory(api, type, userId, searchName))

    fun getCacheTypeRecipePagedList(onLoadMoreFromCache: (start: Int, size: Int, (List<FullRecipeInfo>) -> Unit) -> Unit) =
        buildPagedList(CacheTypeRecipeSourceFactory(onLoadMoreFromCache))

    fun getNetworkTypeRecipePagedList(type: RecipeType, userId: Int,
                                      handleInitialLoadResponse: (List<FullRecipeInfo>) -> Unit,
                                      handleAdditionalLoadResponse: (List<FullRecipeInfo>) -> Unit)
            : Listing<FullRecipeInfo> {
        val networkSourceFactory = NetworkTypeRecipeSourceFactory(api, type, userId,
                handleInitialLoadResponse, handleAdditionalLoadResponse)
        return buildPagedList(networkSourceFactory)
    }

    private fun buildPagedList(sourceFactory: StateSourceFactory<Int, FullRecipeInfo>): Listing<FullRecipeInfo> {
        val pagedList = LivePagedListBuilder(sourceFactory, config)
                .build()

        return Listing(
                pagedList = pagedList,
                initialLoadState = sourceFactory.getInitialLoadState(),
                additionalLoadState = sourceFactory.getAdditionalLoadState(),
                refresh = { pagedList.value?.dataSource?.invalidate() })
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}