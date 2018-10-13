package com.sugar.steptofood.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.TabsActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    companion object {
        fun getInstance() = LoginFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        buttonRegistration.setOnClickListener { toRegistration(it) }
        buttonLogin.setOnClickListener { login(it) }

    }

    override fun getLayout(): Int = R.layout.fragment_login

    private fun login(view: View) {
        //TODO logic enter if login, pass -> success
        val intent = Intent(activity, TabsActivity::class.java)
        startActivity(intent)
    }

    private fun toRegistration(view: View) {
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
                .addSharedElement(
                        activity?.findViewById(R.id.loginTextView)!!,
                        getString(R.string.transition_name_login))
                .addSharedElement(
                        activity?.findViewById(R.id.passTextView)!!,
                        getString(R.string.transition_name_pass))
                .addSharedElement(
                        activity?.findViewById(R.id.nameInputText)!!,
                        getString(R.string.transition_name_name))
    }

    private fun setTransitionAnimation(fragment: Fragment) {
        fragment.sharedElementEnterTransition = ChangeBounds()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
    }
}
