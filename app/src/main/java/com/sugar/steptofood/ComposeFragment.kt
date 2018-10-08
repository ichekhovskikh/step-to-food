package com.sugar.steptofood

import android.os.Bundle
import android.view.View

class ComposeFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayout(): Int = R.layout.fragment_compose

    companion object {
        const val FRAGMENT_TAG = "COMPOSE"
    }
}
