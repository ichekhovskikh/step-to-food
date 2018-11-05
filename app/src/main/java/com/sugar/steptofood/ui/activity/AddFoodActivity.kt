package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.presenter.FoodPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView
import com.sugar.steptofood.utils.ExtraName.PRODUCT
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.item_add_product.*
import kotlinx.android.synthetic.main.item_edit_energy.*
import kotlinx.android.synthetic.main.item_edit_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*
import com.sugar.steptofood.utils.showKeyboard
import com.sugar.steptofood.utils.validateTextView
import kotlinx.android.synthetic.main.action_bar_edit.*
import javax.inject.Inject

class AddFoodActivity : FoodView, AppCompatActivity() {

    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { FoodPresenter(this, api, this) }
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_food)
        initActionBar()
        initEditFoodView()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_edit)
        setActionOnClickButtons()
    }

    private fun getCreatedFood(): Food {
        val food = Food()
        food.name = foodNameTextView.text.toString()
        food.image = imageUri.toString()
        food.description = descriptionTextView.text.toString()
        food.calorie =  calorieTextView.text.toString().toDouble()
        food.protein = proteinTextView.text.toString().toDouble()
        food.fat = fatTextView.text.toString().toDouble()
        food.carbohydrates = carbohydratesTextView.text.toString().toDouble()

        for (i in 0 until productContainer.childCount) {
            val view = productContainer.getChildAt(i)
            val weightEditText: EditText = view.findViewById(R.id.weightEditText)

            val product = Product()
            product.id = view.tag.toString().toInt()
            product.weight = weightEditText.text.toString().toInt()

            (food.products as MutableList).add(product)
        }
        return food
    }

    private fun setActionOnClickButtons() {
        buttonDone.setOnClickListener {
            if (allFieldsAreFilled())
                presenter.addFood(getCreatedFood(), ::showSuccessToastAndExit)
        }
        buttonCancel.setOnClickListener { finish() }
    }

    private fun initEditFoodView() {
        addEditContainersOnView()

        userImageView.visibility = View.VISIBLE
        foodNameTextView.hint = getString(R.string.input_food_name)
        userNameTextView.text = getString(R.string.select_food_image)

        buttonEditDescription.setOnClickListener {
            descriptionTextView.requestFocus()
            showKeyboard(this)
        }

        buttonAddProduct.setOnClickListener {
            val intent = Intent(this, SearchProductActivity::class.java)
            startActivityForResult(intent, GET_PRODUCT)
        }

        userImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

    @SuppressLint("InflateParams")
    private fun addEditContainersOnView() {
        addEditButton()
        val productsContainer = layoutInflater.inflate(R.layout.item_products_container, null)
        val addRowButton = layoutInflater.inflate(R.layout.item_add_product, null)
        val howDoFoodView = layoutInflater.inflate(R.layout.item_edit_how_cook, null)
        val energyFoodView = layoutInflater.inflate(R.layout.item_edit_energy, null)

        foodInfoLayout.addView(productsContainer)
        foodInfoLayout.addView(addRowButton)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)
    }

    @SuppressLint("InflateParams")
    private fun addEditButton() {
        val buttonEdit = layoutInflater.inflate(R.layout.button_edit, null)
        buttonEdit?.setOnClickListener {
            foodNameTextView.requestFocus()
            showKeyboard(this)
        }
        imageActionContainer.addView(buttonEdit)
    }

    private fun allFieldsAreFilled(): Boolean {
        val errorMsg = getString(R.string.error_text_input)

        return (validateImage(errorMsg) &&
                validateProductsList(errorMsg) &&
                validateTextView(descriptionTextView, errorMsg) &&
                validateTextView(calorieTextView, errorMsg) &&
                validateTextView(proteinTextView, errorMsg) &&
                validateTextView(fatTextView, errorMsg) &&
                validateTextView(carbohydratesTextView, errorMsg))
    }

    private fun validateImage(errorMsg: String): Boolean {
        if (imageUri == null) {
            userNameTextView.error = errorMsg
            return false
        }
        return true
    }

    private fun validateProductsList(errorMsg: String): Boolean {
        if (productContainer.childCount == 0) {
            productsLabel.error = errorMsg
            return false
        } else {
            for (i in 0 until productContainer.childCount) {
                val view = productContainer.getChildAt(i)
                val weightEditText: EditText = view.findViewById(R.id.weightEditText)
                if (!validateTextView(weightEditText, errorMsg)) {
                    return false
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PRODUCT && resultCode == Activity.RESULT_OK) {
            addProduct(data)
        } else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            addPhoto(data)
        }
    }

    @SuppressLint("InflateParams")
    private fun addProduct(intent: Intent?) {
        val product: Product = intent?.getSerializableExtra(PRODUCT) as Product
        val productItem = layoutInflater.inflate(R.layout.item_edit_product, null)
        val productNameView: TextView = productItem.findViewById(R.id.productNameTextView)
        productItem.tag = product.id
        productNameView.text = product.name
        productContainer.addView(productItem)
        productsLabel.error = null
    }

    private fun addPhoto(intent: Intent?) {
        imageUri = intent!!.data
        userImageView.setImageURI(imageUri)
        foodImageView.setImageURI(imageUri)
        userNameTextView.error = null
    }

    private fun showSuccessToastAndExit() {
        Toast.makeText(this, getString(R.string.add_food_success), Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val GET_PRODUCT = 1
        const val PICK_IMAGE = 2
    }
}
