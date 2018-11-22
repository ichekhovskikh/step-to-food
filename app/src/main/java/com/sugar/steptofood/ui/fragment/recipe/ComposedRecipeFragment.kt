package com.sugar.steptofood.ui.fragment.recipe

import kotlinx.android.synthetic.main.fragment_recipe_list.*
import android.view.ViewGroup
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.paging.Listing
import com.sugar.steptofood.paging.adapter.*
import com.sugar.steptofood.utils.ExtraName.PRODUCTS

class ComposedRecipeFragment : BaseRecipeFragment() {

    private val products by lazy { activity!!.intent.getSerializableExtra(PRODUCTS) as List<FullProductInfo> }

    companion object {
        fun getInstance() = ComposedRecipeFragment()
    }

    override fun initHeader() {
        (header.parent as ViewGroup).removeView(header)
    }

    override fun createRecipeAdapter(): BaseRecipeAdapter? =
            ComposedRecipeAdapter(this.context!!,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    override fun createCacheRecipePagedList(): Listing<FullRecipeInfo>? = null

    override fun createNetworkRecipePagedList() =
        recipeViewModel.getComposedRecipePagedList(products)
}
