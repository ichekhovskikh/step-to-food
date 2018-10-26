package com.sugar.steptofood.paging.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.sugar.steptofood.model.Food

class FoodAdapter(diffCallback: DiffUtil.ItemCallback<Food>,
                   context: Context,
                   onFoodImageClick: ((Food) -> Unit)? = {},
                   onUserNameClick: ((Food) -> Unit)? = {},
                   onRemoveClick: ((Food) -> Unit)? = {},
                   onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseRecipeAdapter(diffCallback, context, onFoodImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        bindFoodViewHolder(holder, position)
        setFoodViewListeners(holder)
    }
}