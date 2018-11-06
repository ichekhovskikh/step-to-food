package com.sugar.steptofood.ui.fragment.compose

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.sugar.steptofood.R
import com.sugar.steptofood.adapter.HandwrittenListAdapter
import com.sugar.steptofood.extension.getAll
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.ui.activity.SearchProductActivity
import kotlinx.android.synthetic.main.fragment_compose.*
import com.sugar.steptofood.ui.activity.TabsActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.fragment.recipes.ComposedFoodFragment
import com.sugar.steptofood.utils.ExtraName.PRODUCT
import com.sugar.steptofood.utils.ExtraName.PRODUCTS

class ComposeFragment : BaseFragment() {

    private lateinit var adapter: HandwrittenListAdapter<Product>

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initProductList(view)
        initAddButton()
        initSearchButton()
    }

    override fun getLayout(): Int = R.layout.fragment_compose

    private fun initProductList(view: View) {
        adapter = HandwrittenListAdapter(view.context)
        productListView.adapter = adapter

        productListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, parent, position, id ->
            adapter.remove(adapter.getItem(position))
        }
    }

    private fun initAddButton() {
        addProductButton.setOnClickListener {
            val intent = Intent(activity, SearchProductActivity::class.java)
            startActivityForResult(intent, GET_PRODUCT)
        }
    }

    private fun initSearchButton() {
        searchRecipesButton.setOnClickListener {
            activity?.intent?.putExtra(PRODUCTS, adapter.getAll())
            val tabsActivity = (activity as TabsActivity)
            tabsActivity.sectionsPageAdapter.replace(this, ComposedFoodFragment.getInstance())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PRODUCT && resultCode == RESULT_OK) {
            val product = data?.getSerializableExtra(PRODUCT) as Product
            adapter.add(product)
        }
    }

    companion object {
        const val GET_PRODUCT = 1

        fun getInstance() = ComposeFragment()
    }
}
