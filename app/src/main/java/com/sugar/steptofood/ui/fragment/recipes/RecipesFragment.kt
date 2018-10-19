package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.factory.CardViewFactory
import com.sugar.steptofood.ui.activity.AnotherUserActivity
import com.sugar.steptofood.ui.activity.FoodActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.button_like.*
import kotlinx.android.synthetic.main.button_remove.*
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.item_small_food_info.*

open class RecipesFragment : BaseFragment() {

    companion object {
        fun getInstance() = RecipesFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initSearch()
        initAllFoodCards(view)
    }

    override fun getLayout() = R.layout.fragment_recipes

    open fun getRecipes() {
        //TODO recipes for cards
    }

    @SuppressLint("InflateParams")
    private fun initSearch() {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))
        underTabContainer.addView(search)
    }

    protected fun initAllFoodCards(view: View) {
        /* TODO
        for with pagination food in foods
        addFoodCard(contentContainer, food)
        */
        addFoodCard(contentContainer) //TODO test
    }

    private fun addFoodCard(container: ViewGroup) {
        //TODO user, food
        val foodCard = CardViewFactory.createFoodCardView(inflater)
        container.addView(foodCard)
        addButtonInCornerListener()

        foodImageView.setOnClickListener {
            val intent = Intent(activity, FoodActivity::class.java)
            //TODO putExtra(food)
            startActivity(intent)
        }

        textUserNameView.setOnClickListener {
            val intent = Intent(activity, AnotherUserActivity::class.java)
            //TODO putExtra(user)
            startActivity(intent)
        }
    }

    private fun addButtonInCornerListener() {
        addLikeListenerIfButtonExists()
        addRemoveListenerIfButtonExists()
    }

    private fun addLikeListenerIfButtonExists() {
        buttonLike?.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO add/remove from db
        }
    }

    private fun addRemoveListenerIfButtonExists() {
        buttonRemove?.setOnClickListener {
            //TODO remove from db
        }
    }
}
