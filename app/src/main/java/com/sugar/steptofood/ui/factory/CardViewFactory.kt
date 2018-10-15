package com.sugar.steptofood.ui.factory

import android.annotation.SuppressLint
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.widget.*
import com.sugar.steptofood.R

class CardViewFactory private constructor() {

    companion object {

        @SuppressLint("ResourceAsColor", "InflateParams") //TODO annotation remove
        fun createFoodCardView(inflater: LayoutInflater?): CardView {
            //TODO user, food
            val foodCard = inflater?.inflate(R.layout.item_food_card, null) as CardView
            addButtonInCorner(inflater, foodCard)
            //foodCard?.tag = "id"

            val textUserNameView = foodCard.findViewById<TextView>(R.id.textUserNameView)
            //TODO if (you != food user)
            textUserNameView.text = "Иван Чеховских" //TODO test
            textUserNameView.setTextColor(R.color.black) //TODO test
            return foodCard
        }

        @SuppressLint("InflateParams", /*TODO test*/ "ResourceAsColor")
        fun createProductsCardView(inflater: LayoutInflater?): CardView {
            //TODO user, food, products
            val productsCard = inflater?.inflate(R.layout.item_products_card, null) as CardView
            addButtonInCorner(inflater, productsCard)
            //productsCard?.tag = "id"
            //addProducts colored

            val textUserNameView = productsCard.findViewById<TextView>(R.id.textUserNameView)
            //TODO if (you != food user)
            textUserNameView.text = "Иван Чеховских" //TODO test
            textUserNameView.setTextColor(R.color.black) //TODO test
            return productsCard
        }

        private fun addButtonInCorner(inflater: LayoutInflater?, card: CardView) {
            //TODO if (you != food user)
            addLikeButtonInCorner(inflater, card)
            //else addRemoveButtonInCorner(inflater, card)
        }

        @SuppressLint("InflateParams")
        private fun addLikeButtonInCorner(inflater: LayoutInflater?, card: CardView) {
            val button = inflater?.inflate(R.layout.button_like, null) as ToggleButton
            val buttonContainer = card.findViewById<FrameLayout>(R.id.buttonContainer)
            //TODO set initial value like
            buttonContainer?.addView(button)
        }

        @SuppressLint("InflateParams")
        private fun addRemoveButtonInCorner(inflater: LayoutInflater?, card: CardView) {
            val button = inflater?.inflate(R.layout.button_remove, null) as Button
            val buttonContainer = card.findViewById<FrameLayout>(R.id.buttonContainer)
            //TODO set initial value like
            buttonContainer?.addView(button)
        }
    }
}