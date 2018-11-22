package com.sugar.steptofood.paging.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.paging.adapter.holder.*

class ComposedRecipeAdapter(context: Context,
                            session: Session,
                            onRecipeImageClick: ((FullRecipeInfo) -> Unit)? = {},
                            onUserNameClick: ((FullRecipeInfo) -> Unit)? = {},
                            onRemoveClick: ((FullRecipeInfo) -> Unit)? = {},
                            onLikeClick: ((FullRecipeInfo, Boolean) -> Unit)? = { _, _ -> })
    : BaseRecipeAdapter(context, session, onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun getRecipeCardLayout() = R.layout.item_recipe_with_products_card

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADER) NetworkStateItemViewHolder(createNetworkStateItemView(container))
        else ComposedRecipeViewHolder(createRecipeItemView(viewType), inflater)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindRecipeViewHolder(holder, position)
    }
}