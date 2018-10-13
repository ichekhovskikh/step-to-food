package com.sugar.steptofood.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.sugar.steptofood.utils.ExstraName.Companion.PRODUCT
import com.sugar.steptofood.R
import com.sugar.steptofood.adapter.HandwrittenListAdapter
import com.sugar.steptofood.ui.activity.SearchProductActivity
import kotlinx.android.synthetic.main.fragment_compose.*
import com.sugar.steptofood.ui.activity.TabsActivity


class ComposeFragment : BaseFragment() {

    private var productListAdapter: HandwrittenListAdapter? = null

    companion object {
        val GET_PRODUCT = 1

        fun getInstance(): ComposeFragment {
            return ComposeFragment()
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initProductList(view)
        initAddButton(view)
        initSearchButton(view)
    }

    override fun getLayout(): Int = R.layout.fragment_compose

    private fun initProductList(view: View) {
        val productListView = view.findViewById<ListView>(R.id.productListView)
        productListAdapter = HandwrittenListAdapter(view.context)
        productListView.adapter = productListAdapter

        productListView.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView: AdapterView<*>, parent: View, position: Int, id: Long ->
            productListAdapter?.remove(productListAdapter?.getItem(position))
        }
    }

    private fun initAddButton(view: View) {
        addProductButton.setOnClickListener {
            val intent = Intent(activity, SearchProductActivity::class.java)
            //TODO added product yet
            startActivityForResult(intent, GET_PRODUCT)
        }
    }

    private fun initSearchButton(view: View) {
        searchRecipesButton.setOnClickListener {
            /*activity?.intent?.putExtra(PRODUCTS, productListAdapter?.getAllItems())*/
            val tabsActivity = (activity as TabsActivity)
            tabsActivity.sectionsPageAdapter?.replace(this, ComposedFoodsFragment.getInstance())

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_PRODUCT && resultCode == RESULT_OK) {
            //TODO Product product = (Product implements Serializable) intent.getSerializableExtra(PRODUCT);
            productListAdapter?.add(data?.getStringExtra(PRODUCT))
        }
    }
}
