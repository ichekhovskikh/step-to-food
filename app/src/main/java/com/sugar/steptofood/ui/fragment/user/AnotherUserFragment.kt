package com.sugar.steptofood.ui.fragment.user

import android.view.View
import kotlinx.android.synthetic.main.fragment_user.*

class AnotherUserFragment : BaseUserFragment() {

    companion object {
        fun getInstance() = AnotherUserFragment()
    }

    override fun initMenuItems(view: View) {
        initAddedRecipes(itemMenuContainer)
        initLikeRecipes(itemMenuContainer)
    }
}