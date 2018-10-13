package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.support.v7.widget.CardView
import android.widget.Button
import android.widget.FrameLayout
import com.sugar.steptofood.R

class UserRecipesFragment : RecipesFragment() {

    companion object {
        fun getInstance() = UserRecipesFragment()
    }

    override fun getRecipes() {
        //TODO recipes for cards
    }

    @SuppressLint("InflateParams")
    override fun addButtonInCorner(card: CardView) {
        val button = inflater?.inflate(R.layout.button_remove, null) as Button
        //TODO set initial value like
        button.setOnClickListener {
            //TODO remove from db
        }
        val buttonContainer = card.findViewById<FrameLayout>(R.id.buttonContainer)
        buttonContainer?.addView(button)
    }
}