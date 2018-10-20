package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.ui.FoodView
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.button_like.*
import kotlinx.android.synthetic.main.button_remove.*
import kotlinx.android.synthetic.main.fragment_recipes.*

open class RecipesFragment : FoodView, BaseFragment() {

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

    fun refreshFoods(foods: List<Food>) {
    }

    private fun addFoodCard(container: ViewGroup) {
        //TODO user, food
        //TODO replace on adapter
        /*val foodCard = CardViewFactory.createFoodCardView(inflater)
        container.addView(foodCard)
        addButtonInCornerListener()

        foodImageView.setOnClickListener {
            val intent = Intent(view, FoodActivity::class.java)
            //TODO putExtra(food)
            startActivity(intent)
        }

        textUserNameView.setOnClickListener {
            val intent = Intent(view, AnotherUserActivity::class.java)
            //TODO putExtra(user)
            startActivity(intent)
        }*/
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
