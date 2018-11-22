package com.sugar.steptofood.paging.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.model.fullinfo.FullRecipeInfo
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.utils.loadImage

open class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
    val textRecipeNameView: TextView = itemView.findViewById(R.id.textRecipeNameView)
    val textUserNameView: TextView = itemView.findViewById(R.id.textUserNameView)
    val textCalorieNameView: TextView = itemView.findViewById(R.id.textCalorieNameView)
    val buttonLike: ToggleButton? = itemView.findViewById(R.id.buttonLike)
    val buttonRemove: Button? = itemView.findViewById(R.id.buttonRemove)

    open fun bindTo(recipe: FullRecipeInfo?,
                    onRecipeImageClick: ((FullRecipeInfo) -> Unit)?,
                    onUserNameClick: ((FullRecipeInfo) -> Unit)?,
                    onRemoveClick: ((FullRecipeInfo) -> Unit)?,
                    onLikeClick: ((FullRecipeInfo, Boolean) -> Unit)?) {
        if (recipe == null) return
        textRecipeNameView.text = recipe.name
        textCalorieNameView.text = recipe.calorie.toString()
        if (recipe.image == null)
            recipeImageView.setImageBitmap(null)
        else loadImage(recipe.image!!)?.into(recipeImageView)

        if (itemViewType == BaseRecipeAdapter.ANOTHER_USER_RECIPE) {
            textUserNameView.text = recipe.author?.name
            buttonLike?.isChecked = recipe.hasSessionUserLike
        }
        setRecipeViewListeners(recipe, onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick)
    }

    private fun setRecipeViewListeners(recipe: FullRecipeInfo,
                                       onRecipeImageClick: ((FullRecipeInfo) -> Unit)?,
                                       onUserNameClick: ((FullRecipeInfo) -> Unit)?,
                                       onRemoveClick: ((FullRecipeInfo) -> Unit)?,
                                       onLikeClick: ((FullRecipeInfo, Boolean) -> Unit)?) {
        recipeImageView.setOnClickListener {
            onRecipeImageClick?.invoke(recipe)
        }

        if (itemViewType == BaseRecipeAdapter.ANOTHER_USER_RECIPE) {
            textUserNameView.setOnClickListener {
                onUserNameClick?.invoke(recipe)
            }
            buttonLike?.setOnCheckedChangeListener { _, hasLike ->
                onLikeClick?.invoke(recipe, hasLike)
            }
        } else {
            buttonRemove?.setOnClickListener {
                onRemoveClick?.invoke(recipe)
            }
        }
    }
}