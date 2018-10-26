package com.sugar.steptofood.paging

import android.support.v7.util.DiffUtil

import com.sugar.steptofood.model.Food

class FoodDiffUtilCallback : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldFood: Food, newFood: Food): Boolean {
        return oldFood.id == newFood.id
    }

    override fun areContentsTheSame(oldFood: Food, newFood: Food): Boolean {
        return oldFood == newFood
    }
}