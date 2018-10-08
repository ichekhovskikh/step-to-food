package com.sugar.steptofood

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_food.*

class AddFoodActivity : AppCompatActivity() {

    private var inflater: LayoutInflater? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_food)
        initializeActionBar()
        initializeEditFoodView()
    }

    private fun initializeActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_edit)
    }

    @SuppressLint("InflateParams")
    private fun initializeEditFoodView() {
        addEditButton()
        val productsInfoLabel = inflater?.inflate(R.layout.item_products, null)
        val addRowButton = inflater?.inflate(R.layout.item_add_ingredient, null)
        val howDoFoodView = inflater?.inflate(R.layout.item_edit_how_cook, null)
        val energyFoodView = inflater?.inflate(R.layout.item_edit_energy, null)

        foodInfoLayout.addView(productsInfoLabel)
        foodInfoLayout.addView(addRowButton)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)

        foodNameTextView.hint = getString(R.string.input_food_name)
        userNameTextView.text = getString(R.string.select_food_image)
    }

    @SuppressLint("InflateParams")
    private fun addEditButton() {
        val buttonEdit = inflater?.inflate(R.layout.button_edit, null)
        imageActionContainer.addView(buttonEdit)
    }
}
