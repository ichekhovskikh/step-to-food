package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.extension.observe
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.model.User
import com.sugar.steptofood.presenter.RecipePresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.RecipeView
import com.sugar.steptofood.utils.ExtraName.RECIPE_ID
import com.sugar.steptofood.utils.ExtraName.UID
import com.sugar.steptofood.utils.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.item_energy.*
import kotlinx.android.synthetic.main.item_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*
import javax.inject.Inject

class RecipeActivity : RecipeView, AppCompatActivity() {

    @Inject
    lateinit var dbHelper: SQLiteHelper

    @Inject
    lateinit var api: ApiService

    @Inject
    lateinit var session: Session

    private val presenter by lazy { RecipePresenter(this, api, this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_recipe)
        initRecipeView()

        val recipeId = intent.getIntExtra(RECIPE_ID, -1)
        getRecipeFromApiOrCacheIfNoInternet(recipeId)
    }

    private fun getRecipeFromApiOrCacheIfNoInternet(recipeId: Int) {
        if (isNetworkAvailable(this))
            presenter.getRecipe(recipeId).observe(this) { recipe ->
                setRecipe(recipe)
            }
        else onShowError(getString(R.string.error_no_internet))
        setRecipeFromCacheAtLoadTime(recipeId)
    }

    private fun setRecipeFromCacheAtLoadTime(recipeId: Int) {
        val recipe = dbHelper.recipeDao.queryForId(recipeId)
        recipe?.let { setRecipe(recipe) }
    }

    private fun setRecipe(recipe: Recipe) {
        if (isFinishing)
            return

        recipeNameTextView.setText(recipe.name)
        if (recipe.author?.id != session.userId) {
            addLikeButton(recipe)
            setAuthor(recipe.author)
        } else addRemoveButton(recipe)

        for (product in recipe.products!!)
            addProduct(product)
        setRecipeEnergy(recipe)

        presenter.getRecipeImage(recipe.id!!).observe(this) {
            setRecipeImage(it)
        }
    }

    private fun setRecipeEnergy(recipe: Recipe) {
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
        presenter.getRecipeAuthorAvatar(author.id!!).observe(this) {
            setRecipeAuthorAvatar(it)
        }
        userNameTextView.setOnClickListener {
            onUserNameClickListener(author)
        }
        recipeImageView.setOnClickListener {
            onUserNameClickListener(author)
        }
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
    private fun addLikeButton(recipe: Recipe) {
        val buttonLike = layoutInflater.inflate(R.layout.button_like, null) as ToggleButton
        buttonLike.isChecked = recipe.hasYourLike
        buttonLike.setOnCheckedChangeListener { _, hasLike ->
            presenter.setLikeRecipe(recipe.id!!, hasLike)
            dbHelper.recipeBusinessObject.setOrRemoveLike(recipe.id!!, session.userId, hasLike)
        }
        imageActionContainer.addView(buttonLike)
    }

    @SuppressLint("InflateParams")
    private fun addRemoveButton(recipe: Recipe) {
        val buttonRemove = layoutInflater.inflate(R.layout.button_remove, null)
        buttonRemove.setOnClickListener {
            presenter.removeRecipe(recipe.id!!)
            dbHelper.recipeBusinessObject.cascadeRemove(recipe.id!!)
            finish()
        }
        imageActionContainer.addView(buttonRemove)
    }

    private fun onUserNameClickListener(author: User) {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(UID, author.id)
        startActivity(intent)
    }

    private fun setRecipeImage(bitmap: Bitmap?) {
        bitmap?.let { recipeImageView?.setImageBitmap(bitmap) }
    }

    private fun setRecipeAuthorAvatar(bitmap: Bitmap?) {
        bitmap?.let { userImageView?.setImageBitmap(bitmap) }
        userImageView?.visibility = View.VISIBLE
    }

    override fun onShowLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
