package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Recipe
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.RecipeView
import kotlinx.android.synthetic.main.fragment_recipe_list.*
import com.sugar.steptofood.presenter.RecipePresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.activity.UserActivity
import com.sugar.steptofood.ui.activity.RecipeActivity
import com.sugar.steptofood.utils.ExtraName.RECIPE_ID
import com.sugar.steptofood.utils.ExtraName.UID
import javax.inject.Inject
import android.widget.Toast
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.paging.factory.UserRecipeSourceFactory
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.UserRecipeAdapter
import com.sugar.steptofood.utils.RecipeType
import android.view.ViewGroup
import com.sugar.steptofood.extension.afterTextChanged
import com.sugar.steptofood.paging.PagedRecipeRepository
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.utils.isNetworkAvailable

@SuppressLint("InflateParams")
open class RecipeFragment : RecipeView, BaseFragment() {

    @Inject
    protected lateinit var dbHelper: SQLiteHelper

    @Inject
    protected lateinit var api: ApiService

    @Inject
    protected lateinit var session: Session

    protected val presenter by lazy { RecipePresenter(this, api, context!!) }
    private var pagedRecipeRepository: PagedRecipeRepository? = null
    private var adapter: BaseRecipeAdapter? = null

    private val search by lazy { inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar }

    companion object {
        fun getInstance() = RecipeFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)

        if (isNetworkAvailable(context!!))
            dbHelper.recipeBusinessObject.removeAll()

        initHeader()
        initContent()
    }

    override fun getLayout() = R.layout.fragment_recipe_list

    @SuppressLint("InflateParams")
    open fun initHeader() {
        if (search.parent != null)
            (search.parent as ViewGroup).removeView(search)

        search.setHint(getString(R.string.search_recipe))
        search.setPlaceHolder(getString(R.string.search_recipe))
        search.afterTextChanged {
            pagedRecipeRepository?.refreshData(getRecipeSourceFactory())
        }
        tittleTabContainer.addView(search)
    }

    open fun getRecipeType() = RecipeType.RECOMMENDED

    open fun createRecipeAdapter(): BaseRecipeAdapter? =
            UserRecipeAdapter(context!!,
                    api,
                    session,
                    ::onRecipeImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    open fun getRecipeSourceFactory(): BaseRecipeFactory =
            UserRecipeSourceFactory(api, session, dbHelper, getAuthor(), getRecipeType(), search.text)

    private fun getAuthor() = activity!!.intent.getIntExtra(UID, session.userId)

    private fun initContent() {
        adapter = createRecipeAdapter()
        recycler.adapter = adapter

        val sourceFactory = getRecipeSourceFactory()
        pagedRecipeRepository?.removeObservers(this)
        pagedRecipeRepository = PagedRecipeRepository(sourceFactory)
        pagedRecipeRepository?.observe(this, Observer { recipes ->
            adapter?.submitList(recipes)
        })
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
        pagedRecipeRepository?.removeItem(recipe.id!!)
    }

    protected fun onLikeClickListener(recipe: Recipe, hasLike: Boolean) {
        presenter.setLikeRecipe(recipe.id!!, hasLike)
        dbHelper.recipeBusinessObject.setOrRemoveLike(recipe.id!!, session.userId, hasLike)
    }

    override fun onShowLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}
