package com.sugar.steptofood.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.item_food_card.*

class RecipesFragment : BaseFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initSearch(view)
    }

    override fun getLayout() = R.layout.fragment_recipes

    @SuppressLint("InflateParams")
    private fun initSearch(view: View) {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))

        val underTabContainer = view.findViewById<LinearLayout>(R.id.underTabContainer)
        underTabContainer.addView(search)
    }

    @SuppressLint("InflateParams")
    fun addFoodCard(container: ViewGroup) {
        //TODO user, food
        val foodCard = inflater?.inflate(R.layout.item_food_card, null)
        val button: ToggleButton = inflater?.inflate(R.layout.button_like, null) as ToggleButton
        button.setOnCheckedChangeListener { buttonView, isChecked ->  onLikeClick(buttonView, isChecked) }
        buttonContainer.addView(button)
        //TODO onclick userName -> user
        container.addView(foodCard)
    }

    private fun onLikeClick(buttonView: CompoundButton?, isChecked: Boolean) {
        //TODO add/remove from db
    }
}
