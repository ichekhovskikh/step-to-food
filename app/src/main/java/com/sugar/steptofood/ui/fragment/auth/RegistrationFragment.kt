package com.sugar.steptofood.ui.fragment.auth

import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.BaseView
import com.sugar.steptofood.utils.validateTextView
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseView, BaseFragment() {

    companion object {
        fun getInstance() = RegistrationFragment()
    }

    private val startActivity by lazy { activity as StartActivity }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        buttonRegister.setOnClickListener { register() }
    }

    override fun getLayout(): Int = R.layout.fragment_registration

    private fun register() {
        if (validateTextView(nameRegText) && validateTextView(loginRegText) && validateTextView(passRegText)) {
            errorRegMsg.visibility = View.INVISIBLE
            (activity as StartActivity).register(
                    nameRegText.text.toString(),
                    loginRegText.text.toString(),
                    passRegText.text.toString())
        } else onShowError(getString(R.string.error_text_input))
    }

    override fun onShowLoading() {
        startActivity.onShowLoading()
    }

    override fun onHideLoading() {
        startActivity.onHideLoading()
    }

    override fun onShowError(error: String) {
        errorRegMsg.text = error
        errorRegMsg.visibility = View.VISIBLE
    }
}
