package com.sugar.steptofood.ui.fragment.recipe

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import com.sugar.steptofood.ui.activity.*
import com.sugar.steptofood.utils.ExtraName.RECIPE_ID
import com.sugar.steptofood.utils.ExtraName.UID
import android.widget.Toast
import com.sugar.steptofood.paging.adapter.*
import com.sugar.steptofood.model.fullinfo.FullRecipeInfo
import com.sugar.steptofood.paging.Listing
import com.sugar.steptofood.ui.activity.RecipeActivity.Companion.REMOVED_ITEM_RESULT
import com.sugar.steptofood.utils.extension.*
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel
import com.sugar.steptofood.utils.Status

abstract class BaseRecipeFragment : BaseFragment() {

    protected var adapter: BaseRecipeAdapter? = null

    protected val recipeViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }
    protected val currentUserId by lazy { activity!!.intent.getIntExtra(UID, recipeViewModel.session.userId) }

    companion object {
        const val OPEN_RECIPE = 1
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initHeader()
        initContent()
        initLoader()
    }

    override fun getLayout() = R.layout.fragment_recipe_list

    abstract fun initHeader()

    abstract fun createRecipeAdapter(): BaseRecipeAdapter?

    abstract fun createNetworkRecipePagedList(): Listing<FullRecipeInfo>

    abstract fun createCacheRecipePagedList(): Listing<FullRecipeInfo>?

    private fun initContent() {
        adapter = createRecipeAdapter()
        recycler.adapter = adapter
        showCacheRecipeListIfExists {
            refreshNetworkPagedRecipeList()
        }
    }

    fun refreshNetworkPagedRecipeList() {
        val networkList = createNetworkRecipePagedList()

        swipeRefreshLayout.setOnRefreshListener {
            showCacheRecipeListIfExists()
            networkList.refresh.invoke()
        }

        //to call a method initial load in networkList
        networkList.pagedList.observe(this) {}

        networkList.initialLoadState.observe(this) { state ->
            swipeRefreshLayout.setRefreshing(state.status == Status.RUNNING)
            if (state.status == Status.SUCCESS) {
                networkList.pagedList.observe(this) { list ->
                    adapter?.submitList(list)
                }
                networkList.additionalLoadState.observe(this) {
                    adapter?.setLoadState(it)
                }
            } else if (state.status == Status.FAILED) {
                showError(state.msg)
            }
        }
    }

    private fun showCacheRecipeListIfExists(cacheLoadedCallback: () -> Unit = {}) {
        val cache = createCacheRecipePagedList()
        if (cache != null) {
            cache.pagedList.observe(this) { list ->
                adapter?.submitList(list)
            }
            cache.additionalLoadState.observe(this) {
                adapter?.setLoadState(it)
            }
            cache.initialLoadState.observe(this) { state ->
                swipeRefreshLayout.setRefreshing(state.status == Status.RUNNING)
                if (state.status != Status.RUNNING)
                    cacheLoadedCallback.invoke()
            }
            cache.additionalLoadState.observe(this) {
                adapter?.setLoadState(it)
            }
        } else cacheLoadedCallback.invoke()
    }

    protected fun onRecipeImageClickListener(recipe: FullRecipeInfo) {
        val intent = Intent(activity, RecipeActivity::class.java)
        intent.putExtra(RECIPE_ID, recipe.id)
        startActivityForResult(intent, OPEN_RECIPE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == OPEN_RECIPE && resultCode == REMOVED_ITEM_RESULT) {
            val pagedList = adapter?.currentList
            pagedList?.dataSource?.invalidate()
        }
    }

    protected fun onUserNameClickListener(recipe: FullRecipeInfo) {
        val intent = Intent(activity, UserActivity::class.java)
        intent.putExtra(UID, recipe.author?.id)
        startActivity(intent)
    }

    protected fun onRemoveClickListener(recipe: FullRecipeInfo) {
        recipeViewModel.removeRecipe(recipe.id!!) {
            val pagedList = adapter?.currentList
            pagedList?.dataSource?.invalidate()
        }
    }

    protected fun onLikeClickListener(recipe: FullRecipeInfo, hasLike: Boolean) {
        recipeViewModel.setLikeRecipe(recipe.id!!, hasLike)
    }

    private fun initLoader() {
        recipeViewModel.getLoadingStatus().observe(this) { networkState ->
            when (networkState.status) {
                Status.RUNNING -> showLoading()
                Status.SUCCESS -> hideLoading()
                Status.FAILED -> showError(networkState.msg)
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
