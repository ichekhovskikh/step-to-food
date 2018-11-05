package com.sugar.steptofood.paging.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService

class FoodAdapter(context: Context,
                  api: ApiService,
                  session: Session,
                  onFoodImageClick: ((Food) -> Unit)? = {},
                  onUserNameClick: ((Food) -> Unit)? = {},
                  onRemoveClick: ((Food) -> Unit)? = {},
                  onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseRecipeAdapter(context, api, session, onFoodImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun getFoodCardLayout() = R.layout.item_food_card

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        bindFoodViewHolder(holder, position)
        setFoodViewListeners(holder)
    }
}