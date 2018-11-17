package com.sugar.steptofood.ui.fragment.recipe

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import com.sugar.steptofood.ui.activity.UserActivity
import com.sugar.steptofood.ui.activity.RecipeActivity
import com.sugar.steptofood.utils.ExtraName.RECIPE_ID
import com.sugar.steptofood.utils.ExtraName.UID
import android.widget.Toast
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.UserRecipeAdapter
import com.sugar.steptofood.utils.RecipeType
import android.view.ViewGroup
import com.sugar.steptofood.extension.afterTextChanged
import com.sugar.steptofood.extension.observe
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel

@SuppressLint("InflateParams")
open class RecipeFragment : BaseFragment() {

    protected val recipeViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }
    protected var adapter: BaseRecipeAdapter? = null

    private val search by lazy { inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar }

    companion object {
        fun getInstance() = RecipeFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initHeader()
        initContent()
        initLoader()
        initErrorObserver()
    }

    override fun getLayout() = R.layout.fragment_recipe_list

    @SuppressLint("InflateParams")
    open fun initHeader() {
        if (search.parent != null)
            (search.parent as ViewGroup).removeView(search)

        search.setHint(getString(R.string.search_recipe))
        search.setPlaceHolder(getString(R.string.search_recipe))
        search.afterTextChanged {
            refreshPagedRecipeList()
        }
        tittleTabContainer.addView(search)
    }

    open fun getRecipeType() = RecipeType.RECOMMENDED

    open fun createRecipeAdapter(): BaseRecipeAdapter? =
            UserRecipeAdapter(context!!,
                    recipeViewModel.appDatabase,
                    recipeViewModel.session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    open fun refreshPagedRecipeList() {
        recipeViewModel.getPagedList(getRecipeType(), getUser(), search.text)
                .observe(this) { pagedList ->
                    adapter?.submitList(pagedList)
                }
    }

    private fun getUser() = activity!!.intent.getIntExtra(UID, recipeViewModel.session.userId)

    private fun initContent() {
        adapter = createRecipeAdapter()
        recycler.adapter = adapter
        refreshPagedRecipeList()
    }

    protected fun onRecipeImageClickListener(recipe: Recipe) {
        val intent = Intent(activity, RecipeActivity::class.java)
        intent.putExtra(RECIPE_ID, recipe.id)
        startActivity(intent)
    }

    protected fun onUserNameClickListener(recipe: Recipe) {
        val intent = Intent(activity, UserActivity::class.java)
        intent.putExtra(UID, recipe.author?.id)
        startActivity(intent)
    }

    protected fun onRemoveClickListener(recipe: Recipe) {
        recipeViewModel.removeRecipe(recipe.id!!)
    }

    protected fun onLikeClickListener(recipe: Recipe, hasLike: Boolean) {
        recipeViewModel.setLikeRecipe(recipe.id!!, hasLike)
    }

    private fun initLoader() {
        recipeViewModel.getLoadingStatus().observe(this) { status ->
            when (status) {
                BaseRepository.LoadingStatus.LOADING -> showLoading()
                BaseRepository.LoadingStatus.LOADED -> hideLoading()
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun initErrorObserver() {
        recipeViewModel.getErrorMessage().observe(this) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }
}
