package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.activity_search_product.*
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.sugar.steptofood.model.fullinfo.FullProductInfo
import com.sugar.steptofood.utils.extension.*
import com.sugar.steptofood.ui.viewmodel.ProductViewModel
import com.sugar.steptofood.utils.ExtraName.PRODUCT
import com.sugar.steptofood.utils.Status
import kotlinx.android.synthetic.main.item_search.*

class SearchProductActivity : AppCompatActivity() {

    private val productViewModel by lazy { ViewModelProviders.of(this).get(ProductViewModel::class.java) }
    private lateinit var adapter: ArrayAdapter<FullProductInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        initSearch()
        initProductList()
        initLoader()

        productViewModel.getAll().observe(this) { products ->
            refreshProducts(products)
        }
    }

    private fun initSearch() {
        search.afterTextChanged {name ->
            productViewModel.search(name).observe(this) { products ->
                refreshProducts(products)
            }
        }
    }

    private fun refreshProducts(products: List<FullProductInfo>) {
        adapter.clear()
        adapter.addAll(products)
    }

    private fun initProductList() {
        adapter = ArrayAdapter(searchResultListView.context, android.R.layout.simple_list_item_1)
        searchResultListView.adapter = adapter

        searchResultListView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val returnIntent = Intent()
                    returnIntent.putExtra(PRODUCT, adapter.getItem(position))
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
    }

    private fun initLoader() {
        productViewModel.getLoadingStatus().observe(this) { networkState ->
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
