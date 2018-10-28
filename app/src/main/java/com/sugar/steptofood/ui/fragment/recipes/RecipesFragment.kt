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
import android.arch.paging.LivePagedListBuilder
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.widget.Toast
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.paging.FoodDiffUtilCallback
import com.sugar.steptofood.paging.factory.FoodSourceFactory
import com.sugar.steptofood.paging.adapter.BaseRecipeAdapter
import com.sugar.steptofood.paging.adapter.FoodAdapter
import com.sugar.steptofood.utils.FoodType
import io.reactivex.disposables.CompositeDisposable

open class RecipesFragment : FoodView, BaseFragment() {

    @Inject
    lateinit var dbHelper: SQLiteHelper

    @Inject
    lateinit var api: ApiService

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    protected val presenter by lazy { FoodPresenter(this, api) }
    private var adapter: BaseRecipeAdapter? = null

    companion object {
        fun getInstance() = RecipesFragment()

        const val PAGE_SIZE = 10
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        initHeader()
        initContent()
    }

    override fun getLayout() = R.layout.fragment_recipes

    @SuppressLint("InflateParams")
    open fun initHeader() {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))
        tittleTabContainer.addView(search)
    }

    open fun getFoodType() = FoodType.RECOMMENDED

    open fun createFoodAdapter(): BaseRecipeAdapter? =
            FoodAdapter(FoodDiffUtilCallback(),
                    this.context!!,
                    session,
                    ::onFoodImageClickListener,
                    ::onUserNameClickListener,
                    ::onRemoveClickListener,
                    ::onLikeClickListener)

    open fun getFoodSourceFactory(): DataSource.Factory<Int, Food> =
            FoodSourceFactory(api, compositeDisposable, getAuthor(), getFoodType(), dbHelper)

    private fun getAuthor() = activity!!.intent.getIntExtra(UID, session.userId)

    private fun initContent() {
        val sourceFactory = getFoodSourceFactory()
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build()

        val pagedListLiveData = LivePagedListBuilder(sourceFactory, config).build()

        adapter = createFoodAdapter()
        pagedListLiveData.observe(this, Observer<PagedList<Food>> { foods ->
            adapter?.submitList(foods)
        })
        recycler.adapter = adapter
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
        presenter.removeFood(food.id!!)
    }

    fun onLikeClickListener(food: Food, hasLike: Boolean) {
        presenter.setLikeFood(food.id!!, hasLike)
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
