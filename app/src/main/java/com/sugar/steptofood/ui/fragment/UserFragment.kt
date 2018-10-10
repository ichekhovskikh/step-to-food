package com.sugar.steptofood.ui.fragment

import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.BaseFragment

class UserFragment : BaseFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun getLayout(): Int = R.layout.fragment_user

    companion object {
        const val FRAGMENT_TAG = "USER"
    }
}
