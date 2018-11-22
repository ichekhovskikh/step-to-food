package com.sugar.steptofood.paging.adapter

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.model.dto.EntityDto
import com.sugar.steptofood.model.fullinfo.FullRecipeInfo
import com.sugar.steptofood.paging.adapter.holder.*
import com.sugar.steptofood.utils.NetworkState

abstract class BaseRecipeAdapter(context: Context,
                                 private val session: Session,
                                 private val onRecipeImageClick: ((FullRecipeInfo) -> Unit)?,
                                 private val onUserNameClick: ((FullRecipeInfo) -> Unit)?,
                                 private val onRemoveClick: ((FullRecipeInfo) -> Unit)?,
                                 private val onLikeClick: ((FullRecipeInfo, Boolean) -> Unit)?)
    : PagedListAdapter<FullRecipeInfo, RecyclerView.ViewHolder>(RecipeDiffUtilCallback()) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    private var loadState: NetworkState? = null

    abstract fun getRecipeCardLayout(): Int

    fun createRecipeItemView(viewType: Int): View {
        val recipeCard = inflater.inflate(getRecipeCardLayout(), null)
        val buttonInCorner = if (viewType == SESSION_USER_RECIPE) removeButtonInCorner() else likeButtonInCorner()
        val buttonContainer: FrameLayout = recipeCard.findViewById(R.id.buttonContainer)
        buttonContainer.addView(buttonInCorner)
        return recipeCard
    }

    fun createNetworkStateItemView(parent: ViewGroup): View =
            inflater.inflate(R.layout.item_load_state, parent, false)

    protected fun bindRecipeViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            LOADER -> (holder as NetworkStateItemViewHolder).bindTo(loadState)
            else -> (holder as RecipeViewHolder).bindTo(getItem(position),
                    onRecipeImageClick, onUserNameClick, onRemoveClick, onLikeClick)
        }
    }

    fun setLoadState(loadState: NetworkState?) {
        val previousState = this.loadState
        val hadExtraRow = hasExtraRow()
        this.loadState = loadState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != loadState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = loadState != null && loadState != NetworkState.LOADED

    override fun getItemViewType(position: Int) =
            if (hasExtraRow() && position == itemCount - 1)
                LOADER
            else if (getItem(position)!!.author?.id == session.userId)
                SESSION_USER_RECIPE
            else ANOTHER_USER_RECIPE

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

    @SuppressLint("InflateParams")
    protected fun likeButtonInCorner() = inflater.inflate(R.layout.button_like, null) as ToggleButton

    @SuppressLint("InflateParams")
    protected fun removeButtonInCorner() = inflater.inflate(R.layout.button_remove, null) as Button

    companion object {
        const val SESSION_USER_RECIPE = 0
        const val ANOTHER_USER_RECIPE = 1
        const val LOADER = 2
    }

    private class RecipeDiffUtilCallback<T : EntityDto> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldEntity: T, newEntity: T): Boolean {
            return oldEntity.id == newEntity.id
        }

        override fun areContentsTheSame(oldEntity: T, newEntity: T): Boolean {
            return oldEntity == newEntity
        }
    }
}