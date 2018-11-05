package com.sugar.steptofood.paging.adapter

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.downloadSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.rest.ApiService

abstract class BaseRecipeAdapter(context: Context,
                                 private val api: ApiService,
                                 private val session: Session,
                                 private val onFoodImageClick: ((Food) -> Unit)?,
                                 private val onUserNameClick: ((Food) -> Unit)?,
                                 private val onRemoveClick: ((Food) -> Unit)?,
                                 private val onLikeClick: ((Food, Boolean) -> Unit)?)
    : PagedListAdapter<Food, BaseRecipeAdapter.FoodViewHolder>(FoodDiffUtilCallback()) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    abstract fun getFoodCardLayout(): Int

    @SuppressLint("InflateParams")
    protected fun createItemView(viewType: Int): View {
        val foodCard = inflater.inflate(getFoodCardLayout(), null)
        val buttonInCorner = if (viewType == YOUR_FOOD) removeButtonInCorner() else likeButtonInCorner()
        val buttonContainer: FrameLayout = foodCard.findViewById(R.id.buttonContainer)
        buttonContainer.addView(buttonInCorner)
        return foodCard
    }

    protected fun bindFoodViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)!!
        holder.textFoodNameView.text = food.name
        holder.textCalorieNameView.text = food.calorie.toString()

        api.getFoodImage(food.id!!)
                .downloadSubscribe({
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(it.byteStream())
                    if (bitmap != null)
                        holder.foodImageView.setImageBitmap(bitmap)
                }, {})

        if (holder.itemViewType == ANOTHER_USER_FOOD) {
            holder.textUserNameView.text = food.author?.name
            holder.buttonLike?.isChecked = food.hasYourLike
        }
    }

    protected fun setFoodViewListeners(holder: FoodViewHolder) {
        val food = getItem(holder.adapterPosition)!!
        holder.foodImageView.setOnClickListener {
            onFoodImageClick?.invoke(food)
        }

        if (holder.itemViewType == ANOTHER_USER_FOOD) {
            holder.textUserNameView.setOnClickListener {
                onUserNameClick?.invoke(food)
            }
            holder.buttonLike?.setOnCheckedChangeListener { buttonView, hasLike ->
                onLikeClick?.invoke(food, hasLike)
            }
        } else {
            holder.buttonRemove?.setOnClickListener {
                onRemoveClick?.invoke(food)
            }
        }
    }

    override fun getItemViewType(position: Int) =
            if (getItem(position)!!.author?.id == session.userId)
                YOUR_FOOD
            else ANOTHER_USER_FOOD

    @SuppressLint("InflateParams")
    protected fun likeButtonInCorner() = inflater.inflate(R.layout.button_like, null) as ToggleButton

    @SuppressLint("InflateParams")
    protected fun removeButtonInCorner() = inflater.inflate(R.layout.button_remove, null) as Button

    companion object {
        const val ANOTHER_USER_FOOD = 0
        const val YOUR_FOOD = 1
    }

    open class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImageView: ImageView = itemView.findViewById(R.id.foodImageView)
        val textFoodNameView: TextView = itemView.findViewById(R.id.textFoodNameView)
        val textUserNameView: TextView = itemView.findViewById(R.id.textUserNameView)
        val textCalorieNameView: TextView = itemView.findViewById(R.id.textCalorieNameView)
        val buttonLike: ToggleButton? = itemView.findViewById(R.id.buttonLike)
        val buttonRemove: Button? = itemView.findViewById(R.id.buttonRemove)
    }

    private class FoodDiffUtilCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood.id == newFood.id
        }

        override fun areContentsTheSame(oldFood: Food, newFood: Food): Boolean {
            return oldFood == newFood
        }
    }
}