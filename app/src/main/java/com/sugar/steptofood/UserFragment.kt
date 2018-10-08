package com.sugar.steptofood

import android.os.Bundle
import android.view.View

class UserFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayout(): Int = R.layout.fragment_user

    companion object {
        const val FRAGMENT_TAG = "USER"
    }
}
