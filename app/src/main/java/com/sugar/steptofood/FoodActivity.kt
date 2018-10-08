package com.sugar.steptofood

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_food.*

class FoodActivity : AppCompatActivity() {
    private var inflater: LayoutInflater? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_food)
        initFoodInfoView()
    }

    @SuppressLint("InflateParams")
    private fun initFoodInfoView() {
        addLikeButton()
        val productsInfoLabel = inflater?.inflate(R.layout.item_products, null)
        val addRowButton = inflater?.inflate(R.layout.item_ingredient, null)
        val howDoFoodView = inflater?.inflate(R.layout.item_how_cook, null)
        val energyFoodView = inflater?.inflate(R.layout.item_energy, null)

        foodInfoLayout.addView(productsInfoLabel)
        foodInfoLayout.addView(addRowButton)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)

        foodNameTextView.keyListener = null
        foodNameTextView.hint = "Гречка"
        userNameTextView.text = "Иван Чеховских"
    }

    @SuppressLint("InflateParams")
    private fun addLikeButton() {
        val buttonLike = inflater?.inflate(R.layout.button_like, null)
        imageActionContainer.addView(buttonLike)
    }
}
