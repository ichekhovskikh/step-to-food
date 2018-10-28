package com.sugar.steptofood.paging.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product

class ComposedFoodAdapter(diffCallback: DiffUtil.ItemCallback<Food>,
                          context: Context,
                          session: Session,
                          onFoodImageClick: ((Food) -> Unit)? = {},
                          onUserNameClick: ((Food) -> Unit)? = {},
                          onRemoveClick: ((Food) -> Unit)? = {},
                          onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseRecipeAdapter(diffCallback, context, session, onFoodImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): FoodViewHolder {
        return ComposedFoodViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        bindFoodViewHolder(holder, position)
        setFoodViewListeners(holder)

        if (holder is ComposedFoodViewHolder) {
            val products = getItem(position)!!.products!!.sortedWith(compareBy { it.includedInSearch })
            for (product in products) {
                holder.productContainer.addView(createProductView(product))
            }
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

    class ComposedFoodViewHolder(view: View) : FoodViewHolder(view) {
        val productContainer: LinearLayout = itemView.findViewById(R.id.productContainer)
    }
}