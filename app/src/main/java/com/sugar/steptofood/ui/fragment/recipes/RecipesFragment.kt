package com.sugar.steptofood.ui.fragment.recipes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mancj.materialsearchbar.MaterialSearchBar
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.FoodView
import kotlinx.android.synthetic.main.fragment_recipes.*
import com.sugar.steptofood.presenter.FoodPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.activity.AnotherUserActivity
import com.sugar.steptofood.ui.activity.FoodActivity
import com.sugar.steptofood.utils.ExtraName.FOOD_ID
import com.sugar.steptofood.utils.ExtraName.UID
import javax.inject.Inject
import android.arch.paging.PagedList
import android.arch.lifecycle.Observer
import android.widget.Toast
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.paging.factory.FoodSourceFactory
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.FoodAdapter
import com.sugar.steptofood.utils.FoodType
import android.view.ViewGroup
import com.sugar.steptofood.extension.afterTextChanged
import com.sugar.steptofood.paging.PagedFoodRepository
import com.sugar.steptofood.paging.factory.BaseRecipeFactory
import com.sugar.steptofood.utils.isNetworkAvailable

@SuppressLint("InflateParams")
open class RecipesFragment : FoodView, BaseFragment() {

    @Inject
    protected lateinit var dbHelper: SQLiteHelper

    @Inject
    protected lateinit var api: ApiService

    @Inject
    protected lateinit var session: Session

    protected val presenter by lazy { FoodPresenter(this, api, context!!) }
    private var pagedFoodRepository: PagedFoodRepository? = null
    private var adapter: BaseRecipeAdapter? = null
    private lateinit var adapterObserver: Observer<PagedList<Food>>

    private val search by lazy { inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar }

    companion object {
        fun getInstance() = RecipesFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)

        if (isNetworkAvailable(context!!))
            dbHelper.foodBusinessObject.removeAll()

        initHeader()
        initContent()
    }

    override fun getLayout() = R.layout.fragment_recipes

    @SuppressLint("InflateParams")
    open fun initHeader() {
        if (search.parent != null)
            (search.parent as ViewGroup).removeView(search)

        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))
        search.afterTextChanged {
            pagedFoodRepository?.refreshData(getFoodSourceFactory())
        }
        tittleTabContainer.addView(search)
    }

    open fun getFoodType() = FoodType.RECOMMENDED

    open fun createFoodAdapter(): BaseRecipeAdapter? =
            FoodAdapter(this.context!!,
                    api,
                    session,
                    ::onFoodImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    open fun getFoodSourceFactory(): BaseRecipeFactory =
            FoodSourceFactory(api, session, getAuthor(), getFoodType(), dbHelper, getSearchString())

    private fun getSearchString() = search.text

    private fun getAuthor() = activity!!.intent.getIntExtra(UID, session.userId)

    private fun initContent() {
        adapter = createFoodAdapter()
        adapterObserver = Observer { foods ->
            adapter?.submitList(foods)
        }
        recycler.adapter = adapter

        val sourceFactory = getFoodSourceFactory()
        pagedFoodRepository = PagedFoodRepository(sourceFactory)
        pagedFoodRepository?.observe(this, adapterObserver)
    }

    fun onFoodImageClickListener(food: Food) {
        val intent = Intent(activity, FoodActivity::class.java)
        intent.putExtra(FOOD_ID, food.id)
        startActivity(intent)
    }

    fun onUserNameClickListener(food: Food) {
        val intent = Intent(activity, AnotherUserActivity::class.java)
        intent.putExtra(UID, food.author?.id)
        startActivity(intent)
    }

    fun onRemoveClickListener(food: Food) {
        pagedFoodRepository?.removeItem(food.id!!)
    }

    fun onLikeClickListener(food: Food, hasLike: Boolean) {
        presenter.setLikeFood(food.id!!, hasLike)
        dbHelper.foodBusinessObject.setOrRemoveLike(food, session.userId, hasLike)
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}
