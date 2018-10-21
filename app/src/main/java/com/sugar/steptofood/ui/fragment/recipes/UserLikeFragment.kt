package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.widget.TextView
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.fragment_recipes.*

class UserLikeFragment : RecipesFragment() {

    companion object {
        fun getInstance() = UserLikeFragment()
    }

    override fun getRecipes() {
        //TODO recipes for cards
    }

    @SuppressLint("InflateParams")
    override fun initHeader() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.like_recipes_tittle)
        tittleTabContainer.addView(title)
    }
}