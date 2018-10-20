package com.sugar.steptofood.ui.fragment.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.activity.TabsActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    companion object {
        fun getInstance() = LoginFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        buttonRegistration.setOnClickListener { toRegistration() }
        buttonLogin.setOnClickListener { login() }
    }

    override fun getLayout(): Int = R.layout.fragment_login

    private fun login() {
        //TODO logic enter if login, pass -> success
        (activity as StartActivity).login()
    }

    private fun toRegistration() {
        val registration = RegistrationFragment.getInstance()
        setTransitionAnimation(registration)
        setTransitionAnimation(this)

        getTransactionWithSharedElements()
                .replace(R.id.fragmentContainer, registration)
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
}
