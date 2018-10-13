package com.sugar.steptofood.ui.fragment

import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : BaseFragment() {

    companion object {
        fun getInstance() = RegistrationFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        buttonRegistration.setOnClickListener { registration(it) }

    }

    override fun getLayout(): Int = R.layout.fragment_registration

    fun registration(view: View) {
        //TODO enter if login, pass not exist in db -> login
        fragmentManager?.popBackStack()
    }
}
