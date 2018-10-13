package com.sugar.steptofood.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.auth.LoginFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
