package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.presenter.LoginPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.LoginView
import com.sugar.steptofood.ui.fragment.auth.LoginFragment
import javax.inject.Inject

class StartActivity : LoginView, AppCompatActivity() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { LoginPresenter(this, api, session) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        //TODO logic enter if login, pass -> success
        setContentView(R.layout.activity_start)
        setFragment(LoginFragment.getInstance())
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }

    override fun login() {
        val intent = Intent(this, TabsActivity::class.java)
        startActivity(intent)
    }
}
