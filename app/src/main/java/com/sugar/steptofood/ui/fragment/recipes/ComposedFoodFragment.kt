package com.sugar.steptofood.ui.fragment.recipes

import com.sugar.steptofood.adapter.ComposedFoodAdapter
import com.sugar.steptofood.model.Food
import kotlinx.android.synthetic.main.fragment_recipes.*
import android.view.ViewGroup

class ComposedFoodFragment : RecipesFragment() {
    private var adapter: ComposedFoodAdapter? = null

    companion object {
        fun getInstance() = ComposedFoodFragment()
    }

    override fun getRecipes() {
        //TODO presenter.getComposedFood(products)
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
