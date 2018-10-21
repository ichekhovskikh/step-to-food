package com.sugar.steptofood.ui.view

import com.sugar.steptofood.model.Food

interface FoodView : BaseView {
    fun refreshFoods(food: List<Food>) {}
}