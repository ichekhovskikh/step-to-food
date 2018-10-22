package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.presenter.FoodPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView
import kotlinx.android.synthetic.main.activity_food.*
import javax.inject.Inject

class FoodActivity : FoodView, AppCompatActivity() {

    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { FoodPresenter(this, api) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        //TODO getExtra(food)
        setContentView(R.layout.activity_food)
        initFoodInfoView()
    }

    @SuppressLint("InflateParams")
    private fun initFoodInfoView() {
        addLikeButton()
        val productsInfoLabel = layoutInflater.inflate(R.layout.item_products_container, null)
        val addRowButton = layoutInflater.inflate(R.layout.item_product, null)
        val howDoFoodView = layoutInflater.inflate(R.layout.item_how_cook, null)
        val energyFoodView = layoutInflater.inflate(R.layout.item_energy, null)

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
        val buttonLike = layoutInflater.inflate(R.layout.button_like, null)
        imageActionContainer.addView(buttonLike)
        //TODO set initial value
    }

    override fun onShowError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
