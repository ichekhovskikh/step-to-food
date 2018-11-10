package com.sugar.steptofood.ui.fragment.recipe

import kotlinx.android.synthetic.main.fragment_recipe_list.*
import android.view.ViewGroup
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.ComposedRecipeAdapter
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.utils.ExtraName.PRODUCTS
import com.sugar.steptofood.utils.RecipeType

class ComposedRecipeFragment : RecipeFragment() {
    companion object {
        fun getInstance() = ComposedRecipeFragment()
    }

    override fun initHeader() {
        (header.parent as ViewGroup).removeView(header)
    }

    override fun getRecipeSourceFactory(): BaseRecipeFactory {
        val products = activity!!.intent.getSerializableExtra(PRODUCTS) as List<Product>
        return recipeViewModel.getRecipeSourceFactory(products)
    }

    override fun getRecipeType() = RecipeType.COMPOSED

    override fun createRecipeAdapter(): BaseRecipeAdapter? =
            ComposedRecipeAdapter(this.context!!,
                    recipeViewModel.api,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)
}
