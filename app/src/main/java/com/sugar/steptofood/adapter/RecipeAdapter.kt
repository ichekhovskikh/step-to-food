package com.sugar.steptofood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food

class RecipeAdapter(context: Context,
                    private val onFoodImageClick: ((Food) -> Unit)? = {},
                    private val onUserNameClick: ((Food) -> Unit)? = {},
                    private val onRemoveClick: ((Food) -> Unit)? = {},
                    private val onLikeClick: ((Food, Boolean) -> Unit)? = { food, hasLike -> })
    : BaseRecyclerAdapter<Food, RecipeAdapter.RecipesViewHolder>(context) {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecipesViewHolder {
        val foodCard = inflater.inflate(R.layout.item_food_card, null)
        val buttonInCorner = if (viewType == YOUR_FOOD) removeButtonInCorner() else likeButtonInCorner()
        val buttonContainer: FrameLayout = foodCard.findViewById(R.id.buttonContainer)
        buttonContainer.addView(buttonInCorner)
        return RecipesViewHolder(foodCard)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val food = items[position]
        holder.textFoodNameView.text = food.name
        holder.textCalorieNameView.text = food.calorie.toString()
        holder.foodImageView.setImageURI(Uri.parse(food.image))

        if (holder.itemViewType == ANOTHER_USER_FOOD) {
            holder.textUserNameView.text = food.author?.name
            holder.buttonLike.isChecked = food.hasYourLike
        }
    }

    override fun getItemViewType(position: Int) =
            if (items[position].isYourAdded)
                YOUR_FOOD
            else ANOTHER_USER_FOOD

    @SuppressLint("InflateParams")
    private fun likeButtonInCorner() = inflater?.inflate(R.layout.button_like, null) as ToggleButton

    @SuppressLint("InflateParams")
    private fun removeButtonInCorner() = inflater?.inflate(R.layout.button_remove, null) as Button

    companion object {
        const val ANOTHER_USER_FOOD = 0
        const val YOUR_FOOD = 1
    }

    inner class RecipesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImageView: ImageView = itemView.findViewById(R.id.foodImageView)
        val textFoodNameView: TextView = itemView.findViewById(R.id.textFoodNameView)
        val textUserNameView: TextView = itemView.findViewById(R.id.textUserNameView)
        val textCalorieNameView: TextView = itemView.findViewById(R.id.textCalorieNameView)
        val buttonLike: ToggleButton = itemView.findViewById(R.id.buttonLike)
        val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)

        init {
            foodImageView.setOnClickListener {
                onFoodImageClick?.invoke(items[adapterPosition])
            }

            if (itemViewType == ANOTHER_USER_FOOD) {
                textUserNameView.setOnClickListener {
                    onUserNameClick?.invoke(items[adapterPosition])
                }
                buttonRemove.setOnClickListener {
                    onRemoveClick?.invoke(items[adapterPosition])
                }
            } else buttonLike.setOnCheckedChangeListener { buttonView, hasLike ->
                onLikeClick?.invoke(items[adapterPosition], hasLike)
            }
        }
    }
}
