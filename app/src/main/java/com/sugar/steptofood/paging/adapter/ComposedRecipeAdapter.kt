package com.sugar.steptofood.paging.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.*

class ComposedRecipeAdapter(context: Context,
                            session: Session,
                            onRecipeImageClick: ((Recipe) -> Unit)? = {},
                            onUserNameClick: ((Recipe) -> Unit)? = {},
                            onRemoveClick: ((Recipe) -> Unit)? = {},
                            onLikeClick: ((Recipe, Boolean) -> Unit)? = { _, _ -> })
    : BaseRecipeAdapter(context, session, onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun getRecipeCardLayout() = R.layout.item_recipe_with_products_card

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecipeViewHolder {
        return ComposedRecipeViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)!!
        bindRecipeViewHolder(holder, recipe)
        if (holder is ComposedRecipeViewHolder) {
            val products = recipe.products!!.sortedWith(compareBy { !it.includedInSearch })
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

    class ComposedRecipeViewHolder(view: View) : RecipeViewHolder(view) {
        val productContainer: LinearLayout = itemView.findViewById(R.id.productContainer)
    }
}