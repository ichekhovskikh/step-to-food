package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.UserItemActivity
import kotlinx.android.synthetic.main.fragment_recipes.*

class UserLikeFragment : RecipesFragment() {

    companion object {
        fun getInstance() = UserLikeFragment()
    }

    override fun getRecipes() {
        val userId = (activity as UserItemActivity).userId
        presenter.getLikeFoods(userId!!)
    }

    @SuppressLint("InflateParams")
    override fun initHeader() {
        val title = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        title.text = getString(R.string.like_recipes_tittle)
        tittleTabContainer.addView(title)
    }
}