package com.sugar.steptofood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import com.sugar.steptofood.adapter.holder.FoodViewHolder
import com.sugar.steptofood.model.Food

class RecipeAdapter(context: Context,
                    onFoodImageClick: ((Food) -> Unit)? = {},
                    onUserNameClick: ((Food) -> Unit)? = {},
                    onRemoveClick: ((Food) -> Unit)? = {},
                    onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseFoodAdapter<FoodViewHolder>(context, onFoodImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        bindFoodViewHolder(holder, position)
        setFoodViewListeners(holder)
    }
}
