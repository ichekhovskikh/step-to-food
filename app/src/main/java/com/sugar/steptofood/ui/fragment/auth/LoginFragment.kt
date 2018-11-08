package com.sugar.steptofood.ui.fragment.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sugar.steptofood.R
import com.sugar.steptofood.extension.validate
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.activity.StartActivity.Companion.REG_TAG
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.BaseView
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseView, BaseFragment() {

    companion object {
        fun getInstance() = LoginFragment()
    }

    private val startActivity by lazy { activity as StartActivity }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        buttonRegistration.setOnClickListener { toRegistration() }
        buttonLogin.setOnClickListener { login() }
    }

    override fun getLayout(): Int = R.layout.fragment_login

    private fun login() {
        if (loginLogText.validate() && passLogText.validate()) {
            errorLogMsg.visibility = View.INVISIBLE
            startActivity.checkLoginAndPassword(
                    loginLogText.text.toString(), passLogText.text.toString())
        } else onShowError(getString(R.string.error_text_input))
    }

    private fun toRegistration() {
        val registration = RegistrationFragment.getInstance()
        setTransitionAnimation(registration)
        setTransitionAnimation(this)

        getTransactionWithSharedElements()
                .replace(R.id.fragmentContainer, registration, REG_TAG)
                .addToBackStack(this.tag)
                .commit()
    }

    @SuppressLint("CommitTransaction")
    private fun getTransactionWithSharedElements(): FragmentTransaction {
        return fragmentManager!!
                .beginTransaction()
                .addSharedElement(loginTextView, getString(R.string.transition_name_login))
                .addSharedElement(passTextView, getString(R.string.transition_name_pass))
                .addSharedElement(nameInputText, getString(R.string.transition_name_name))
    }

    private fun setTransitionAnimation(fragment: Fragment) {
        fragment.sharedElementEnterTransition = ChangeBounds()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
    }

    override fun onShowLoading() {
        startActivity.onShowLoading()
    }

    override fun onHideLoading() {
        startActivity.onHideLoading()
    }

    override fun onShowError(error: String) {
        errorLogMsg?.text = error
        errorLogMsg?.visibility = View.VISIBLE
    }
}
