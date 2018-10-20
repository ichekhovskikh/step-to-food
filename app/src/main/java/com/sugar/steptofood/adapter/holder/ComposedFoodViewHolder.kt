package com.sugar.steptofood.adapter.holder

import android.view.View
import android.widget.*
import com.sugar.steptofood.R

class ComposedFoodViewHolder(view: View) : FoodViewHolder(view) {
    val productContainer: LinearLayout = itemView.findViewById(R.id.productContainer)
}