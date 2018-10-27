package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.activity_search_product.*
import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.sugar.steptofood.App
import com.sugar.steptofood.extension.afterTextChanged
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.presenter.ProductPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.ProductView
import com.sugar.steptofood.utils.ExtraName.PRODUCT
import kotlinx.android.synthetic.main.item_search.*
import javax.inject.Inject

class SearchProductActivity : ProductView, AppCompatActivity() {

    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { ProductPresenter(this, api) }
    private var adapter: ArrayAdapter<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_search_product)
        initSearch()
        initProductList()
        presenter.getAllProducts()
    }

    override fun refreshProducts(products: List<Product>) {
        adapter?.clear()
        adapter?.addAll(products)
    }

    private fun initSearch() {
        search.afterTextChanged {name ->
            presenter.searchProducts(name)
        }
    }

    private fun initProductList() {
        adapter = ArrayAdapter(searchResultListView.context, android.R.layout.simple_list_item_1)
        searchResultListView.adapter = adapter

        searchResultListView.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, parent, position, id ->
                    val returnIntent = Intent()
                    returnIntent.putExtra(PRODUCT, adapter?.getItem(position))
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
