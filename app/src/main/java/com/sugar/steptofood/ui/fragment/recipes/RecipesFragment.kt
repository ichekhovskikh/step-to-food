package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.FoodActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_recipes.*

open class RecipesFragment : BaseFragment() {

    companion object {
        fun getInstance(): RecipesFragment {
            return RecipesFragment()
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initSearch(view)
        addFoodCard(contentContainer) //TODO test
        initAllFoodCards(view)
    }

    override fun getLayout() = R.layout.fragment_recipes

    open fun getRecipes() {
        //TODO recipes for cards
    }

    @SuppressLint("InflateParams")
    open fun addButtonInCorner(card: CardView) {
        val button = inflater?.inflate(R.layout.button_like, null) as ToggleButton
        //TODO set initial value like
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO add/remove from db
        }
        val buttonContainer = card.findViewById<FrameLayout>(R.id.buttonContainer)
        buttonContainer?.addView(button)
    }

    @SuppressLint("InflateParams")
    private fun initSearch(view: View) {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))

        val underTabContainer = view.findViewById<LinearLayout>(R.id.underTabContainer)
        underTabContainer.addView(search)
    }

    private fun initAllFoodCards(view: View) {
        val contentContainer = view.findViewById<LinearLayout>(R.id.contentContainer)
        /* TODO
        for with pagination food in foods
        addFoodCard(contentContainer, food)
        */
    }

    @SuppressLint("InflateParams")
    private fun addFoodCard(container: ViewGroup) {
        //TODO user, food
        val foodCard = inflater?.inflate(R.layout.item_food_card, null) as CardView
        addButtonInCorner(foodCard)
        addFoodImage(foodCard)
        //TODO onclick userName -> user
//        foodCard?.tag = "id"
        container.addView(foodCard)
    }

    private fun addFoodImage(card: CardView) {
        val foodImageView = card.findViewById<ImageView>(R.id.foodImageView)
        foodImageView.setOnClickListener {
            val intent = Intent(activity, FoodActivity::class.java)
            //TODO putExtra(food)
            startActivity(intent)
        }
    }
}
