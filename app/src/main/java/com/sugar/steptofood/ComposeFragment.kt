package com.sugar.steptofood

import android.os.Bundle
import android.view.View
import android.widget.ListView

class ComposeFragment : BaseFragment() {

    private var productListAdapter: HandwrittenListAdapter? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initProductList(view)
    }

    private fun initProductList(view: View) {
        val productListView = view.findViewById<ListView>(R.id.productListView)
        productListAdapter = HandwrittenListAdapter(view.context)
        productListView.adapter = productListAdapter
    }

    override fun getLayout(): Int = R.layout.fragment_compose

    companion object {
        const val FRAGMENT_TAG = "COMPOSE"
    }
}
