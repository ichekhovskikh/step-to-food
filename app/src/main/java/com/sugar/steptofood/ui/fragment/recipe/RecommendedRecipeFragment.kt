package com.sugar.steptofood.ui.fragment.recipe

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.paging.adapter.*
import com.sugar.steptofood.utils.RecipeType
import com.sugar.steptofood.utils.extension.afterTextChanged
import kotlinx.android.synthetic.main.fragment_recipe_list.*

@SuppressLint("InflateParams")
class RecommendedRecipeFragment : BaseRecipeFragment() {

    private var searchText = ""
    private val search by lazy { inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar }

    companion object {
        fun getInstance() = RecommendedRecipeFragment()
    }

    override fun createRecipeAdapter(): BaseRecipeAdapter? =
            UserRecipeAdapter(context!!,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    override fun createNetworkRecipePagedList() =
            recipeViewModel.getNetworkTypeRecipePagedList(RecipeType.RECOMMENDED, currentUserId, searchText)

    override fun createCacheRecipePagedList() =
            recipeViewModel.getCacheTypeRecipePagedList(RecipeType.RECOMMENDED, currentUserId, searchText)

    @SuppressLint("InflateParams")
    override fun initHeader() {
        if (search.parent != null)
            (search.parent as ViewGroup).removeView(search)

        search.setHint(getString(R.string.search_recipe))
        search.setPlaceHolder(getString(R.string.search_recipe))
        tittleTabContainer.addView(search)

        search.afterTextChanged {
            if (it != searchText) {
                searchText = it
                refreshNetworkPagedRecipeList()
            }
        }
    }
}