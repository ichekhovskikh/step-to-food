package com.sugar.steptofood.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.net.Uri
import android.os.Build
import android.support.annotation.ColorRes
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.TextValidator
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.item_add_product.*
import kotlinx.android.synthetic.main.item_edit_energy.*
import kotlinx.android.synthetic.main.item_edit_how_cook.*
import kotlinx.android.synthetic.main.item_products_container.*
import com.sugar.steptofood.utils.KeyboardUtils

class AddFoodActivity : AppCompatActivity() {

    companion object {
        val GET_PRODUCT = 1
        val PICK_IMAGE = 2
    }

    private var inflater: LayoutInflater? = null

    private var imageUri: Uri? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_food)
        initActionBar()
        initEditFoodView()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_edit)
        setActionOnClickButtons(supportActionBar!!.customView)
    }

    private fun setActionOnClickButtons(view: View) {
        val buttonDone = view.findViewById<Button>(R.id.buttonDone)
        buttonDone.setOnClickListener {
            //TODO if success add to db
            if (allFieldsAreFilled())
                finish()
        }

        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        buttonCancel.setOnClickListener { finish() }
    }

    @SuppressLint("InflateParams")
    private fun initEditFoodView() {
        addEditContentOnView()

        foodNameTextView.hint = getString(R.string.input_food_name)
        userNameTextView.text = getString(R.string.select_food_image)

        buttonEditDescription.setOnClickListener {
            descriptionTextView.requestFocus()
            KeyboardUtils.showKeyboard(this, descriptionTextView)
        }

        buttonAddProduct.setOnClickListener {
            val intent = Intent(this, SearchProductActivity::class.java)
            //TODO added product yet
            startActivityForResult(intent, GET_PRODUCT)
        }

        userImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

    private fun addEditContentOnView() {
        addEditButton()
        val productsContainer = inflater?.inflate(R.layout.item_products_container, null)
        val addRowButton = inflater?.inflate(R.layout.item_add_product, null)
        val howDoFoodView = inflater?.inflate(R.layout.item_edit_how_cook, null)
        val energyFoodView = inflater?.inflate(R.layout.item_edit_energy, null)

        foodInfoLayout.addView(productsContainer)
        foodInfoLayout.addView(addRowButton)
        foodInfoLayout.addView(howDoFoodView)
        foodInfoLayout.addView(energyFoodView)
    }

    @SuppressLint("InflateParams")
    private fun addEditButton() {
        val buttonEdit = inflater?.inflate(R.layout.button_edit, null)
        buttonEdit?.setOnClickListener {
            foodNameTextView.requestFocus()
            KeyboardUtils.showKeyboard(this, foodNameTextView)
        }
        imageActionContainer.addView(buttonEdit)
    }

    private fun allFieldsAreFilled(): Boolean {
        val errorMsg = getString(R.string.error_text_input)

        return (validateImage() &&
                validateProductsList(errorMsg) &&
                TextValidator.validate(descriptionTextView, errorMsg) &&
                TextValidator.validate(energyTextView, errorMsg) &&
                TextValidator.validate(proteinTextView, errorMsg) &&
                TextValidator.validate(fatTextView, errorMsg) &&
                TextValidator.validate(carbohydratesTextView, errorMsg))
    }

    private fun validateImage(): Boolean {
        if (imageUri == null) {
            setSupportColorTextView(userNameTextView, R.color.red)
            return false
        }
        return true
    }

    private fun validateProductsList(errorMsg: String): Boolean {
        if (productContainer.childCount == 0) {
            setSupportColorTextView(productsLabel, R.color.red)
            return false
        } else {
            for (i in 0 until productContainer.childCount) {
                val view = productContainer.getChildAt(i)
                val weightEditText = view.findViewById<EditText>(R.id.weightEditText)
                if (!TextValidator.validate(weightEditText, errorMsg)) {
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
    private fun addProduct(data: Intent?) {
        //TODO Product product = (Product implements Serializable) intent.getSerializableExtra(PRODUCT);
        //data?.getStringExtra(ExstraName.PRODUCT) to item_product
        val product = inflater?.inflate(R.layout.item_product, null)
        productContainer.addView(product)
        setSupportColorTextView(productsLabel, R.color.colorPrimary)
    }

    private fun addPhoto(data: Intent?) {
        imageUri = data!!.data
        userImageView.setImageURI(imageUri)
        foodImageView.setImageURI(imageUri)
        setSupportColorTextView(userNameTextView, R.color.white)
        //val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
    }

    private fun setSupportColorTextView(textView: TextView, @ColorRes id: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(getColor(id))
        }
    }
}
