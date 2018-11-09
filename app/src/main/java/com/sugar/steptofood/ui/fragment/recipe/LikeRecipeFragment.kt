package com.sugar.steptofood.ui.fragment.recipe

import android.annotation.SuppressLint
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.RecipeType
import kotlinx.android.synthetic.main.fragment_recipe_list.*

class LikeRecipeFragment : RecipeFragment() {

    companion object {
        fun getInstance() = LikeRecipeFragment()
    }

    override fun getRecipeType() = RecipeType.LIKE

    @SuppressLint("InflateParams")
    override fun initHeader() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.like_recipes_tittle)
        tittleTabContainer.addView(title)
    }
}