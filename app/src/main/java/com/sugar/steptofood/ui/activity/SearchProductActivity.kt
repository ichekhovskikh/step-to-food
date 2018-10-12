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
import com.sugar.steptofood.ExstraName.Companion.PRODUCT


class SearchProductActivity : AppCompatActivity() {

    //TODO replace string on Product
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)
        initProductList()
    }

    private fun initProductList() {
        adapter = ArrayAdapter(searchResultListView.context, android.R.layout.simple_list_item_1)
        searchResultListView.adapter = adapter

        searchResultListView.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView: AdapterView<*>, parent: View, position: Int, id: Long ->
            val returnIntent = Intent()
            returnIntent.putExtra(PRODUCT, adapter?.getItem(position))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    fun addProducts() {
        /* TODO pagination
        adapter.addAll(db.Products)
        */
    }
}
