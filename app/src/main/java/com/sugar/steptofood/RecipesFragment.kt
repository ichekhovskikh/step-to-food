package com.sugar.steptofood

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mancj.materialsearchbar.MaterialSearchBar

class RecipesFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearch(view)
    }

    @SuppressLint("InflateParams")
    private fun initSearch(view: View) {
        val search = inflater?.inflate(R.layout.item_search, null) as MaterialSearchBar
        search.setHint(getString(R.string.search_food))
        search.setPlaceHolder(getString(R.string.search_food))

        val underTabContainer = view.findViewById<LinearLayout>(R.id.underTabContainer)
        underTabContainer.addView(search)
    }

    override fun getLayout() = R.layout.fragment_recipes

    companion object {
        const val FRAGMENT_TAG = "RECIPES"
    }
}
