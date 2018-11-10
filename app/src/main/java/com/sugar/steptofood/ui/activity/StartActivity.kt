package com.sugar.steptofood.ui.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.extension.observe
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.ui.fragment.auth.LoginFragment
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private val userViewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_fragment)
        initLoader()
        initErrorObserver()
        userViewModel.login(::login)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }

    fun register(name: String, login: String, pass: String) {
        userViewModel.register(name, login, pass, ::login)
    }

    fun checkLoginAndPassword(login: String, pass: String) {
        userViewModel.login(login, pass, ::login)
    }

    fun getErrorMessage(): LiveData<String> {
        return userViewModel.getErrorMessage()
    }

    private fun login() {
        val intent = Intent(this, TabsActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginWindow() {
        setContentView(R.layout.activity_start)
        setFragment(LoginFragment.getInstance())
    }

    private fun initLoader() {
        userViewModel.getLoadingStatus().observe(this) { status ->
            when (status) {
                BaseRepository.LoadingStatus.LOADING -> showLoading()
                BaseRepository.LoadingStatus.LOADED -> hideLoading()
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun initErrorObserver() {
        userViewModel.getErrorMessage().observe(this) {
            if (supportFragmentManager.backStackEntryCount == 0)
                openLoginWindow()
        }
    }
}
