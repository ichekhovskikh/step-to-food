package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.presenter.LoginPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.LoginView
import com.sugar.steptofood.ui.fragment.auth.LoginFragment
import com.sugar.steptofood.ui.view.BaseView
import kotlinx.android.synthetic.main.activity_start.*
import javax.inject.Inject

class StartActivity : LoginView, AppCompatActivity() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { LoginPresenter(this, api, session) }
    private var operationTag: String = LOG_TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_start)
        openLoginWindow()
        presenter.login()
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment, LOG_TAG)
                .commit()
    }

    private fun openLoginWindow() {
        setFragment(LoginFragment.getInstance())
    }

    fun register(name: String, login: String, pass: String) {
        operationTag = REG_TAG
        presenter.register(name, login, pass)
    }

    fun checkLoginAndPassword(login: String, pass: String) {
        operationTag = LOG_TAG
        presenter.login(login, pass)
    }

    override fun login() {
        val intent = Intent(this, TabsActivity::class.java)
        startActivity(intent)
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        val showingView = supportFragmentManager.findFragmentByTag(operationTag) as BaseView?
        showingView?.onShowError(error)
    }

    companion object {
        const val LOG_TAG = "login"
        const val REG_TAG = "registration"
    }
}
