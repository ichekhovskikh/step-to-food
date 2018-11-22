package com.sugar.steptofood.ui.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sugar.steptofood.R
import com.sugar.steptofood.utils.extension.observe
import com.sugar.steptofood.ui.fragment.auth.LoginFragment
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import com.sugar.steptofood.utils.Status
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private val userViewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
    private val errorMessage by lazy { MutableLiveData<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_fragment)
        initLoader()
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

    fun getErrorMessage(): LiveData<String> = errorMessage

    private fun login() {
        val intent = Intent(this, TabsActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginWindow() {
        setContentView(R.layout.activity_start)
        setFragment(LoginFragment.getInstance())
    }

    private fun initLoader() {
        userViewModel.getLoadingStatus().observe(this) { networkState ->
            when (networkState.status) {
                Status.RUNNING -> showLoading()
                Status.SUCCESS -> hideLoading()
                Status.FAILED -> showError(networkState.msg)
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun showError(message: String?) {
        if (supportFragmentManager.backStackEntryCount == 0)
            openLoginWindow()
        errorMessage.postValue(message)
    }
}
