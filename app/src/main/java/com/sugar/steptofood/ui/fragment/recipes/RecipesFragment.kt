package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.FoodView
import kotlinx.android.synthetic.main.fragment_recipes.*
import com.sugar.steptofood.adapter.RecipeAdapter
import com.sugar.steptofood.ui.activity.AnotherUserActivity
import com.sugar.steptofood.ui.activity.FoodActivity

open class RecipesFragment : FoodView, BaseFragment() {
    private var adapter: RecipeAdapter? = null

    companion object {
        fun getInstance() = RecipesFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initHeader()
        initContent()
    }

    override fun getLayout() = R.layout.fragment_recipes

    open fun getRecipes() {
        //TODO presenter.getRecommendedFood()
    }

    @SuppressLint("InflateParams")
    open fun initHeader() {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))
        tittleTabContainer.addView(search)
    }

    open fun initContent() {
        adapter = RecipeAdapter(this.context!!,
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

    fun onFoodImageClickListener(food: Food) {
        val intent = Intent(activity, FoodActivity::class.java)
        //TODO putExtra(food)
        startActivity(intent)
    }

    fun onUserNameClickListener(food: Food) {
        val intent = Intent(activity, AnotherUserActivity::class.java)
        //TODO putExtra(user)
        startActivity(intent)
    }

    fun onRemoveClickListener(food: Food) {
        //TODO remove from db
    }

    fun onLikeClickListener(food: Food, hasLike: Boolean) {
        //TODO add/remove from db
    }
}
