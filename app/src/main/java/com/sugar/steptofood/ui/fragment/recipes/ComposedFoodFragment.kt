package com.sugar.steptofood.ui.fragment.recipes

import com.sugar.steptofood.adapter.ComposedFoodAdapter
import com.sugar.steptofood.model.Food
import kotlinx.android.synthetic.main.fragment_recipes.*
import android.view.ViewGroup
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.utils.ExtraName.PRODUCTS

class ComposedFoodFragment : RecipesFragment() {
    private var adapter: ComposedFoodAdapter? = null

    companion object {
        fun getInstance() = ComposedFoodFragment()
    }

    override fun getRecipes() {
        val product = activity!!.intent.getSerializableExtra(PRODUCTS) as List<Product>
        presenter.searchFoodsByProduct(product)
    }

    override fun initHeader() {
        (header.parent as ViewGroup).removeView(header)
    }

    override fun initContent() {
        adapter = ComposedFoodAdapter(this.context!!,
                ::onFoodImageClickListener,
                ::onUserNameClickListener,
                ::onRemoveClickListener,
                ::onLikeClickListener)
        recycler.adapter = adapter
        getRecipes()
    }

    override fun refreshFoods(foods: List<Food>) {
        adapter?.addAll(foods)
    }
}
