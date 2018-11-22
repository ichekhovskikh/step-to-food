package com.sugar.steptofood.paging.adapter.holder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.model.fullinfo.*

class ComposedRecipeViewHolder(view: View, private val inflater: LayoutInflater) : RecipeViewHolder(view) {
    val productContainer: LinearLayout = itemView.findViewById(R.id.productContainer)

    override fun bindTo(recipe: FullRecipeInfo?,
                        onRecipeImageClick: ((FullRecipeInfo) -> Unit)?,
                        onUserNameClick: ((FullRecipeInfo) -> Unit)?,
                        onRemoveClick: ((FullRecipeInfo) -> Unit)?,
                        onLikeClick: ((FullRecipeInfo, Boolean) -> Unit)?) {
        if (recipe == null) return
        super.bindTo(recipe, onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick)
        productContainer.removeAllViews()
        val products = recipe.products!!.sortedWith(compareBy { !it.includedInSearch })
        for (product in products) {
            productContainer.addView(createProductView(product))
        }
    }

    @SuppressLint("InflateParams")
    private fun createProductView(product: FullProductInfo): View? {
        val productView = inflater.inflate(R.layout.item_product, null)
        val productNameView: TextView = productView.findViewById(R.id.productNameTextView)
        val weightView: TextView = productView.findViewById(R.id.weightTextView)
        val gramLabel: TextView = productView.findViewById(R.id.gramLabel)

        productNameView.text = product.name
        weightView.text = product.weight.toString()
        colorizeProduct(product, listOf(productNameView, weightView, gramLabel), Color.LTGRAY)
        return productView
    }

    private fun colorizeProduct(product: FullProductInfo, textViewList: List<TextView>, @ColorInt color: Int) {
        if (!product.includedInSearch) {
            textViewList.forEach { it.setTextColor(color) }
        }
    }
}