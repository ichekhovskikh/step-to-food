package com.sugar.steptofood.paging.adapter

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.utils.loadImage

abstract class BaseRecipeAdapter(context: Context,
                                 private val appDatabase: AppDatabase,
                                 private val session: Session,
                                 private val onRecipeImageClick: ((Recipe) -> Unit)?,
                                 private val onUserNameClick: ((Recipe) -> Unit)?,
                                 private val onRemoveClick: ((Recipe) -> Unit)?,
                                 private val onLikeClick: ((Recipe, Boolean) -> Unit)?)
    : PagedListAdapter<Recipe, BaseRecipeAdapter.RecipeViewHolder>(RecipeDiffUtilCallback()) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    abstract fun getRecipeCardLayout(): Int

    @SuppressLint("InflateParams")
    protected fun createItemView(viewType: Int): View {
        val recipeCard = inflater.inflate(getRecipeCardLayout(), null)
        val buttonInCorner = if (viewType == YOUR_RECIPE) removeButtonInCorner() else likeButtonInCorner()
        val buttonContainer: FrameLayout = recipeCard.findViewById(R.id.buttonContainer)
        buttonContainer.addView(buttonInCorner)
        return recipeCard
    }

    protected fun bindRecipeViewHolder(holder: RecipeViewHolder, recipe: Recipe) {
        holder.textRecipeNameView.text = recipe.name
        holder.textCalorieNameView.text = recipe.calorie.toString()
        recipe.image?.let { link -> loadImage(link).into(holder.recipeImageView) }

        if (holder.itemViewType == ANOTHER_USER_RECIPE) {
            holder.textUserNameView.text = recipe.author?.name
            holder.buttonLike?.isChecked = recipe.hasYourLike
        }
        setRecipeViewListeners(holder, recipe)
    }

    private fun setRecipeViewListeners(holder: RecipeViewHolder, recipe: Recipe) {
        holder.recipeImageView.setOnClickListener {
            onRecipeImageClick?.invoke(recipe)
        }

        if (holder.itemViewType == ANOTHER_USER_RECIPE) {
            holder.textUserNameView.setOnClickListener {
                onUserNameClick?.invoke(recipe)
            }
            holder.buttonLike?.setOnCheckedChangeListener { _, hasLike ->
                onLikeClick?.invoke(recipe, hasLike)
            }
        } else {
            holder.buttonRemove?.setOnClickListener {
                onRemoveClick?.invoke(recipe)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val recipe = getItem(position)!!
        appDatabase.businessObject.setRecipeProperty(recipe, session.userId)
        return if (recipe.author?.id == session.userId) YOUR_RECIPE else ANOTHER_USER_RECIPE
    }


    @SuppressLint("InflateParams")
    protected fun likeButtonInCorner() = inflater.inflate(R.layout.button_like, null) as ToggleButton

    @SuppressLint("InflateParams")
    protected fun removeButtonInCorner() = inflater.inflate(R.layout.button_remove, null) as Button

    companion object {
        const val ANOTHER_USER_RECIPE = 0
        const val YOUR_RECIPE = 1
    }

    open class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        val textRecipeNameView: TextView = itemView.findViewById(R.id.textRecipeNameView)
        val textUserNameView: TextView = itemView.findViewById(R.id.textUserNameView)
        val textCalorieNameView: TextView = itemView.findViewById(R.id.textCalorieNameView)
        val buttonLike: ToggleButton? = itemView.findViewById(R.id.buttonLike)
        val buttonRemove: Button? = itemView.findViewById(R.id.buttonRemove)
    }

    private class RecipeDiffUtilCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe.id == newRecipe.id
        }

        override fun areContentsTheSame(oldRecipe: Recipe, newRecipe: Recipe): Boolean {
            return oldRecipe == newRecipe
        }
    }
}