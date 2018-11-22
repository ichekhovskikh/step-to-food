package com.sugar.steptofood.ui.fragment.recipe

import android.annotation.SuppressLint
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.UserRecipeAdapter
import com.sugar.steptofood.utils.RecipeType
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class AddedRecipeFragment : BaseRecipeFragment() {

    companion object {
        fun getInstance() = AddedRecipeFragment()
    }

    override fun createRecipeAdapter(): BaseRecipeAdapter? =
            UserRecipeAdapter(context!!,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    override fun createNetworkRecipePagedList() =
            recipeViewModel.getNetworkTypeRecipePagedList(RecipeType.ADDED, currentUserId, "")

    override fun createCacheRecipePagedList() =
            recipeViewModel.getCacheTypeRecipePagedList(RecipeType.ADDED, currentUserId, "")

    @SuppressLint("InflateParams")
    override fun initHeader() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.added_recipes_tittle)
        tittleTabContainer.addView(title)
    }
}