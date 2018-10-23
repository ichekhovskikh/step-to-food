package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.presenter.FoodPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView
import com.sugar.steptofood.utils.ExtraName.FOOD_ID
import com.sugar.steptofood.utils.ExtraName.UID
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.item_energy.*
import kotlinx.android.synthetic.main.item_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*
import javax.inject.Inject

class FoodActivity : FoodView, AppCompatActivity() {

    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { FoodPresenter(this, api) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_food)
        initFoodView()

        val foodId = intent.getIntExtra(FOOD_ID, -1)
        presenter.getFood(foodId, ::setFood)
    }

    fun setFood(food: Food) {
        foodNameTextView.setText(food.name)

        if (!food.isYourAdded) {
            addLikeButton(food)
            userNameTextView.text = food.author?.name
            userNameTextView.setOnClickListener {
                onUserNameClickListener(food)
            }
        } else addRemoveButton(food)

        for(product in food.products!!)
            addProduct(product)

        descriptionTextView.text = food.description
        calorieTextView.text = food.calorie.toString()
        proteinTextView.text = food.protein.toString()
        fatTextView.text = food.fat.toString()
        carbohydratesTextView.text = food.carbohydrates.toString()

        presenter.getFoodImage(food.id!!, ::setFoodImage)
        //TODO presenter.getUserImage(food.id!!, ::setUserAvatar)
    }

    @SuppressLint("InflateParams")
    private fun initFoodView() {
        val productsInfoLabel = layoutInflater.inflate(R.layout.item_products_container, null)
        val howDoFoodView = layoutInflater.inflate(R.layout.item_how_cook, null)
        val energyFoodView = layoutInflater.inflate(R.layout.item_energy, null)

        foodInfoLayout.addView(productsInfoLabel)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)

        foodNameTextView.keyListener = null
    }

    @SuppressLint("InflateParams")
    private fun addProduct(product: Product) {
        val productItem = layoutInflater.inflate(R.layout.item_product, null)
        val productNameView: TextView = productItem.findViewById(R.id.productNameTextView)
        val productWeightView: TextView = productItem.findViewById(R.id.weightTextView)
        productNameView.text = product.name
        productWeightView.text = product.weight.toString()
        productContainer.addView(productItem)
    }

    @SuppressLint("InflateParams")
    private fun addLikeButton(food: Food) {
        val buttonLike = layoutInflater.inflate(R.layout.button_like, null) as ToggleButton
        buttonLike.isChecked = food.hasYourLike
        buttonLike.setOnCheckedChangeListener { button, hasLike ->
            presenter.setLikeFood(food.id!!, hasLike)
        }
        imageActionContainer.addView(buttonLike)
    }

    @SuppressLint("InflateParams")
    private fun addRemoveButton(food: Food) {
        val buttonRemove = layoutInflater.inflate(R.layout.button_remove, null)
        buttonRemove.setOnClickListener {
            presenter.removeFood(food.id!!)
            finish()
            //TODO what happens on remove?
        }
        imageActionContainer.addView(buttonRemove)
    }

    private fun onUserNameClickListener(food: Food) {
        val intent = Intent(this, AnotherUserActivity::class.java)
        intent.putExtra(UID, food.author?.id)
        startActivity(intent)
    }

    private fun setFoodImage(bitmap: Bitmap) {
        foodImageView.setImageBitmap(bitmap)
    }

    override fun onShowError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
