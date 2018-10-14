package com.sugar.steptofood.ui.fragment.user

import android.view.View
import android.widget.LinearLayout
import com.sugar.steptofood.R

class AnotherUserFragment : UserFragment() {

    companion object {
        fun getInstance() = AnotherUserFragment()
    }

    override fun initMenuItems(view: View) {
        val menuContainer = view.findViewById<LinearLayout>(R.id.itemMenuContainer)
        initAddedRecipes(menuContainer)
        initLikeRecipes(menuContainer)
    }
}