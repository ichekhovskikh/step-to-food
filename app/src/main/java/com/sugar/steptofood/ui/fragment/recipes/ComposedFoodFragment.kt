package com.sugar.steptofood.ui.fragment.recipes

import kotlinx.android.synthetic.main.fragment_recipes.*
import android.view.ViewGroup
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.ComposedFoodAdapter
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.paging.factory.ComposedFoodSourceFactory
import com.sugar.steptofood.utils.ExtraName.PRODUCTS
import com.sugar.steptofood.utils.FoodType

class ComposedFoodFragment : RecipesFragment() {
    companion object {
        fun getInstance() = ComposedFoodFragment()
    }

    override fun initHeader() {
        (header.parent as ViewGroup).removeView(header)
    }

    override fun getFoodSourceFactory(): BaseRecipeFactory {
        val products = activity!!.intent.getSerializableExtra(PRODUCTS) as List<Product>
        return ComposedFoodSourceFactory(api, compositeDisposable, products)
    }

    override fun getFoodType() = FoodType.COMPOSED

    override fun createFoodAdapter(): BaseRecipeAdapter? =
            ComposedFoodAdapter(this.context!!,
                    api,
                    session,
                    ::onFoodImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)
}
