package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.extension.observe
import com.sugar.steptofood.model.dto.User
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel
import com.sugar.steptofood.utils.ExtraName.RECIPE_ID
import com.sugar.steptofood.utils.ExtraName.UID
import com.sugar.steptofood.utils.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.item_energy.*
import kotlinx.android.synthetic.main.item_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*

class RecipeActivity : AppCompatActivity() {

    private val recipeViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val recipeId = intent.getIntExtra(RECIPE_ID, -1)

        initRecipeView()
        initLoader()
        initRecipe(recipeId)
    }

    private fun initRecipe(recipeId: Int) {
        recipeViewModel.getRecipe(recipeId).observe(this) { recipe ->
            setRecipe(recipe)
        }
    }

    private fun setRecipe(recipe: FullRecipeInfo) {
        if (isFinishing)
            return

        recipeNameTextView.setText(recipe.name)
        if (recipe.author?.id != recipeViewModel.session.userId) {
            addLikeButton(recipe)
            setAuthor(recipe.author)
        } else addRemoveButton(recipe)

        (productContainer as ViewGroup).removeAllViews()
        for (product in recipe.products!!)
            addProduct(product)
        setRecipeEnergy(recipe)

        recipe.image?.let { loadImage(it)?.into(recipeImageView) }
    }

    private fun setRecipeEnergy(recipe: FullRecipeInfo) {
        descriptionTextView.text = recipe.description
        calorieTextView.text = recipe.calorie.toString()
        proteinTextView.text = recipe.protein.toString()
        fatTextView.text = recipe.fat.toString()
        carbohydratesTextView.text = recipe.carbohydrates.toString()
    }

    @SuppressLint("InflateParams")
    private fun initRecipeView() {
        val productsInfoLabel = layoutInflater.inflate(R.layout.item_products_container, null)
        val howCookView = layoutInflater.inflate(R.layout.item_how_cook, null)
        val energyRecipeView = layoutInflater.inflate(R.layout.item_energy, null)

        recipeInfoLayout.addView(productsInfoLabel)
        recipeInfoLayout.addView(howCookView)
        recipeInfoLayout.addView(energyRecipeView)
        recipeNameTextView.keyListener = null
    }

    private fun setAuthor(author: User?) {
        if (author == null)
            return

        userNameTextView?.text = author.name
        setRecipeAuthorAvatar(author.id!!)

        userNameTextView.setOnClickListener {
            onUserNameClickListener(author)
        }
        recipeImageView.setOnClickListener {
            onUserNameClickListener(author)
        }
    }

    @SuppressLint("InflateParams")
    private fun addProduct(product: FullProductInfo) {
        val productItem = layoutInflater.inflate(R.layout.item_product, null)
        val productNameView: TextView = productItem.findViewById(R.id.productNameTextView)
        val productWeightView: TextView = productItem.findViewById(R.id.weightTextView)
        productNameView.text = product.name
        productWeightView.text = product.weight.toString()
        productContainer.addView(productItem)
    }

    @SuppressLint("InflateParams")
    private fun addLikeButton(recipe: FullRecipeInfo) {
        val buttonLike = layoutInflater.inflate(R.layout.button_like, null) as ToggleButton
        buttonLike.isChecked = recipe.hasSessionUserLike
        buttonLike.setOnCheckedChangeListener { _, hasLike ->
            recipeViewModel.setLikeRecipe(recipe.id!!, hasLike)
        }
        imageActionContainer.addView(buttonLike)
    }

    @SuppressLint("InflateParams")
    private fun addRemoveButton(recipe: FullRecipeInfo) {
        val buttonRemove = layoutInflater.inflate(R.layout.button_remove, null)
        buttonRemove.setOnClickListener {
            recipeViewModel.removeRecipe(recipe.id!!)
            setResult(Activity.RESULT_OK)
            finish()
        }
        imageActionContainer.addView(buttonRemove)
    }

    private fun onUserNameClickListener(author: User) {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(UID, author.id)
        startActivity(intent)
    }

    private fun setRecipeAuthorAvatar(userId: Int) {
        loadAvatar(userId)?.into(userImageView)
        userImageView?.visibility = View.VISIBLE
    }

    private fun initLoader() {
        recipeViewModel.getLoadingStatus().observe(this) { networkState ->
            when (networkState.status) {
                Status.RUNNING -> showLoading()
                Status.SUCCESS -> hideLoading()
                Status.FAILED -> showError(networkState.msg)
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
