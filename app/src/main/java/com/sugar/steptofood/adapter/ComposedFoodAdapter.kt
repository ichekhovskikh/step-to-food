package com.sugar.steptofood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.adapter.holder.ComposedFoodViewHolder
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product

class ComposedFoodAdapter(context: Context,
                          onFoodImageClick: ((Food) -> Unit)? = {},
                          onUserNameClick: ((Food) -> Unit)? = {},
                          onRemoveClick: ((Food) -> Unit)? = {},
                          onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseFoodAdapter<ComposedFoodViewHolder>(context, onFoodImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ComposedFoodViewHolder {
        return ComposedFoodViewHolder(createItemView(viewType))
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: ComposedFoodViewHolder, position: Int) {
        bindFoodViewHolder(holder, position)
        setFoodViewListeners(holder)

        val products = items[position].products!!.sortedWith(compareBy { it.includedInSearch })
        for (product in products) {
            holder.productContainer.addView(createProductView(product))
        }
    }

    @SuppressLint("InflateParams")
    private fun createProductView(product: Product): View? {
        val productView = inflater.inflate(R.layout.item_product, null)
        val productNameView: TextView = productView.findViewById(R.id.productNameTextView)
        val weightView: TextView = productView.findViewById(R.id.weightTextView)

        productNameView.text = product.name
        weightView.text = product.weight.toString()
        if (!product.includedInSearch)
            productNameView.setTextColor(Color.RED)
        return productView
    }
}
