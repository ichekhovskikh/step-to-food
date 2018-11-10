package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sugar.steptofood.R
import com.sugar.steptofood.extension.observe
import com.sugar.steptofood.extension.validate
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel
import com.sugar.steptofood.utils.*
import com.sugar.steptofood.utils.ExtraName.PRODUCT
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.item_add_product.*
import kotlinx.android.synthetic.main.item_edit_energy.*
import kotlinx.android.synthetic.main.item_edit_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*
import kotlinx.android.synthetic.main.action_bar_edit.*

class RecipeCreationActivity : AppCompatActivity() {

    private val recipeViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        initActionBar()
        initEditRecipeView()
        initLoader()
        initErrorObserver()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_edit)
        setActionOnClickButtons()
    }

    private fun getSelectedProducts(): List<Product> {
        val products = mutableListOf<Product>()
        for (i in 0 until productContainer.childCount) {
            val product = Product()

            val view = productContainer.getChildAt(i)
            val weightEditText: EditText = view.findViewById(R.id.weightEditText)
            product.id = view.tag.toString().toInt()
            product.weight = weightEditText.text.toString().toInt()
            products.add(product)
        }
        return products
    }

    private fun setActionOnClickButtons() {
        buttonDone.setOnClickListener {
            if (validateAllFields())
                recipeViewModel.addRecipe(getCreatedRecipe(), ::showSuccessToastAndExit)
        }
        buttonCancel.setOnClickListener { finish() }
    }

    private fun getCreatedRecipe(): Recipe {
        val recipe = Recipe()
        recipe.name = recipeNameTextView.text.toString()
        recipe.image = recipeImageView.contentDescription.toString()
        recipe.description = descriptionTextView.text.toString()
        recipe.calorie = calorieTextView.text.toString().toDouble()
        recipe.protein = proteinTextView.text.toString().toDouble()
        recipe.fat = fatTextView.text.toString().toDouble()
        recipe.carbohydrates = carbohydratesTextView.text.toString().toDouble()
        recipe.products = getSelectedProducts()
        return recipe
    }

    private fun initEditRecipeView() {
        addEditContainersOnView()
        setLabels()
        setClickEditButtonListeners()
        setClickImageListener()
    }

    @SuppressLint("InflateParams")
    private fun addEditContainersOnView() {
        addEditButton()
        val productsContainer = layoutInflater.inflate(R.layout.item_products_container, null)
        val addRowButton = layoutInflater.inflate(R.layout.item_add_product, null)
        val howCookView = layoutInflater.inflate(R.layout.item_edit_how_cook, null)
        val energyRecipeView = layoutInflater.inflate(R.layout.item_edit_energy, null)

        recipeInfoLayout.addView(productsContainer)
        recipeInfoLayout.addView(addRowButton)
        recipeInfoLayout.addView(howCookView)
        recipeInfoLayout.addView(energyRecipeView)
    }

    private fun setLabels() {
        userImageView.visibility = View.VISIBLE
        recipeNameTextView.hint = getString(R.string.input_recipe_name)
        userNameTextView.text = getString(R.string.select_recipe_image)
    }

    private fun setClickEditButtonListeners() {
        buttonEditDescription.setOnClickListener {
            descriptionTextView.requestFocus()
            showKeyboard(this)
        }

        buttonAddProduct.setOnClickListener {
            val intent = Intent(this, SearchProductActivity::class.java)
            startActivityForResult(intent, GET_PRODUCT)
        }
    }

    private fun setClickImageListener() {
        userImageView.setOnClickListener {
            if (hasStoragePermissions(this)) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE)
            } else {
                requestStoragePermissions(this, OPEN_GALLERY_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun addEditButton() {
        val buttonEdit = layoutInflater.inflate(R.layout.button_edit, null)
        buttonEdit?.setOnClickListener {
            recipeNameTextView.requestFocus()
            showKeyboard(this)
        }
        imageActionContainer.addView(buttonEdit)
    }

    private fun validateAllFields(): Boolean {
        val errorMsg = getString(R.string.error_text_input)

        return (validateImage(errorMsg) &&
                validateProductsList(errorMsg) &&
                descriptionTextView.validate(errorMsg) &&
                calorieTextView.validate(errorMsg) &&
                proteinTextView.validate(errorMsg) &&
                fatTextView.validate(errorMsg) &&
                carbohydratesTextView.validate(errorMsg))
    }

    private fun validateImage(errorMsg: String): Boolean {
        if (recipeImageView.contentDescription == null) {
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
                if (!weightEditText.validate(errorMsg)) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPEN_GALLERY_PERMISSIONS_REQUEST_CODE && hasStoragePermissions(this)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }
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
        intent?.data?.let { imageUri ->
            userImageView.setImageURI(imageUri)
            recipeImageView.setImageURI(imageUri)
            recipeImageView.contentDescription = imageUri.toString()
            userNameTextView.error = null
        }
    }

    private fun showSuccessToastAndExit() {
        Toast.makeText(this, getString(R.string.add_recipe_success), Toast.LENGTH_LONG).show()
        finish()
    }

    private fun initLoader() {
        recipeViewModel.getLoadingStatus().observe(this) { status ->
            when (status) {
                BaseRepository.LoadingStatus.LOADING -> showLoading()
                BaseRepository.LoadingStatus.LOADED -> hideLoading()
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun initErrorObserver() {
        recipeViewModel.getErrorMessage().observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val GET_PRODUCT = 1
        const val PICK_IMAGE = 2
    }
}
