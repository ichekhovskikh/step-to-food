package com.sugar.steptofood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.adapter.holder.FoodViewHolder
import com.sugar.steptofood.model.Food

abstract class BaseFoodAdapter<VH : FoodViewHolder>(context: Context,
                                           private val onFoodImageClick: ((Food) -> Unit)?,
                                           private val onUserNameClick: ((Food) -> Unit)?,
                                           private val onRemoveClick: ((Food) -> Unit)?,
                                           private val onLikeClick: ((Food, Boolean) -> Unit)?)
    : BaseRecyclerAdapter<Food, VH>(context) {

    @SuppressLint("InflateParams")
    protected fun createItemView(viewType: Int): View {
        val foodCard = inflater.inflate(R.layout.item_food_card, null)
        val buttonInCorner = if (viewType == YOUR_FOOD) removeButtonInCorner() else likeButtonInCorner()
        val buttonContainer: FrameLayout = foodCard.findViewById(R.id.buttonContainer)
        buttonContainer.addView(buttonInCorner)
        return foodCard
    }

    protected fun bindFoodViewHolder(holder: FoodViewHolder, position: Int) {
        val food = items[position]
        holder.textFoodNameView.text = food.name
        holder.textCalorieNameView.text = food.calorie.toString()
        holder.foodImageView.setImageURI(Uri.parse(food.image))

        if (holder.itemViewType == ANOTHER_USER_FOOD) {
            holder.textUserNameView.text = food.author?.name
            holder.buttonLike.isChecked = food.hasYourLike
        }
    }

    protected fun setFoodViewListeners(holder: FoodViewHolder) {
        holder.foodImageView.setOnClickListener {
            onFoodImageClick?.invoke(items[holder.adapterPosition])
        }

        if (holder.itemViewType == ANOTHER_USER_FOOD) {
            holder.textUserNameView.setOnClickListener {
                onUserNameClick?.invoke(items[holder.adapterPosition])
            }
            holder.buttonRemove.setOnClickListener {
                onRemoveClick?.invoke(items[holder.adapterPosition])
            }
        } else holder.buttonLike.setOnCheckedChangeListener { buttonView, hasLike ->
            onLikeClick?.invoke(items[holder.adapterPosition], hasLike)
        }
    }

    override fun getItemViewType(position: Int) =
            if (items[position].isYourAdded)
                YOUR_FOOD
            else ANOTHER_USER_FOOD

    @SuppressLint("InflateParams")
    protected fun likeButtonInCorner() = inflater?.inflate(R.layout.button_like, null) as ToggleButton

    @SuppressLint("InflateParams")
    protected fun removeButtonInCorner() = inflater?.inflate(R.layout.button_remove, null) as Button

    companion object {
        const val ANOTHER_USER_FOOD = 0
        const val YOUR_FOOD = 1
    }
}
