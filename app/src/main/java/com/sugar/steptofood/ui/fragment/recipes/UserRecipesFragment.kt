package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.fragment_recipes.*

class UserRecipesFragment : RecipesFragment() {

    companion object {
        fun getInstance() = UserRecipesFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initTittle()
        initAllFoodCards(view)
    }

    override fun getRecipes() {
        //TODO recipes for cards
    }

    @SuppressLint("InflateParams")
    private fun initTittle() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.added_recipes_tittle)
        underTabContainer.addView(title)
    }
}