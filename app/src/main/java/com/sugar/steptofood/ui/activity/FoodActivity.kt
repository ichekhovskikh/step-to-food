package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.db.SQLiteHelper
import kotlinx.android.synthetic.main.activity_food.*
import javax.inject.Inject

class FoodActivity : AppCompatActivity() {

    @Inject
    lateinit var dbHelper: SQLiteHelper
    private var inflater: LayoutInflater? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        //TODO getExtra(food)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_food)
        initFoodInfoView()
    }

    @SuppressLint("InflateParams")
    private fun initFoodInfoView() {
        addLikeButton()
        val productsInfoLabel = inflater?.inflate(R.layout.item_products_container, null)
        val addRowButton = inflater?.inflate(R.layout.item_product, null)
        val howDoFoodView = inflater?.inflate(R.layout.item_how_cook, null)
        val energyFoodView = inflater?.inflate(R.layout.item_energy, null)

        foodInfoLayout.addView(productsInfoLabel)
        foodInfoLayout.addView(addRowButton)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)

        foodNameTextView.keyListener = null
        foodNameTextView.setText("Гречка")
        userNameTextView.text = "Иван Чеховских"
        //TODO click name -> user
        //TODO setData
    }

    @SuppressLint("InflateParams")
    private fun addLikeButton() {
        val buttonLike = inflater?.inflate(R.layout.button_like, null)
        imageActionContainer.addView(buttonLike)
    }
}
