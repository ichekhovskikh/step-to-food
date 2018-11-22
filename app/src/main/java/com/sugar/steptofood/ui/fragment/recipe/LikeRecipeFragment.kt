package com.sugar.steptofood.ui.fragment.recipe

import android.annotation.SuppressLint
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.paging.adapter.*
import com.sugar.steptofood.utils.RecipeType
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class LikeRecipeFragment : BaseRecipeFragment() {

    companion object {
        fun getInstance() = LikeRecipeFragment()
    }

    override fun createRecipeAdapter(): BaseRecipeAdapter? =
            UserRecipeAdapter(context!!,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    override fun createNetworkRecipePagedList() =
            recipeViewModel.getNetworkTypeRecipePagedList(RecipeType.LIKE, currentUserId, "")

    override fun createCacheRecipePagedList() =
            recipeViewModel.getCacheTypeRecipePagedList(RecipeType.LIKE, currentUserId, "")

    @SuppressLint("InflateParams")
    override fun initHeader() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.like_recipes_tittle)
        tittleTabContainer.addView(title)
    }
}