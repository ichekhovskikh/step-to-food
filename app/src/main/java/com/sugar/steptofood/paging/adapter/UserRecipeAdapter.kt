package com.sugar.steptofood.paging.adapter

import android.content.Context
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.rest.ApiService

class UserRecipeAdapter(context: Context,
                        api: ApiService,
                        session: Session,
                        onRecipeImageClick: ((Recipe) -> Unit)? = {},
                        onUserNameClick: ((Recipe) -> Unit)? = {},
                        onRemoveClick: ((Recipe) -> Unit)? = {},
                        onLikeClick: ((Recipe, Boolean) -> Unit)? = { _, _ -> })
    : BaseRecipeAdapter(context, api, session, onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick) {

    override fun getRecipeCardLayout() = R.layout.item_recipe_card

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(createItemView(viewType))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        bindRecipeViewHolder(holder, position)
        setRecipeViewListeners(holder)
    }
}