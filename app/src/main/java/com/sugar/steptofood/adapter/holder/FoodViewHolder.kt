package com.sugar.steptofood.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.sugar.steptofood.R

open class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val foodImageView: ImageView = itemView.findViewById(R.id.foodImageView)
    val textFoodNameView: TextView = itemView.findViewById(R.id.textFoodNameView)
    val textUserNameView: TextView = itemView.findViewById(R.id.textUserNameView)
    val textCalorieNameView: TextView = itemView.findViewById(R.id.textCalorieNameView)
    val buttonLike: ToggleButton = itemView.findViewById(R.id.buttonLike)
    val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)
}