package com.sugar.steptofood.ui.fragment.auth

import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.extension.*
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment() {

    companion object {
        fun getInstance() = RegistrationFragment()
    }

    private val startActivity by lazy { activity as StartActivity }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initErrorObserver()
        buttonRegister.setOnClickListener { register() }
    }

    override fun getLayout(): Int = R.layout.fragment_registration

    private fun register() {
        if (nameRegText.validate() && loginRegText.validate() && passRegText.validate()) {
            errorRegMsg.visibility = View.INVISIBLE
            startActivity.register(
                    nameRegText.text.toString(),
                    loginRegText.text.toString(),
                    passRegText.text.toString())
        } else showError(getString(R.string.error_text_input))
    }

    private fun initErrorObserver() {
        startActivity.getErrorMessage().observe(this) { message ->
            if (isVisible) showError(message)
        }
    }

    private fun showError(error: String) {
        errorRegMsg?.text = error
        errorRegMsg?.visibility = View.VISIBLE
    }
}
