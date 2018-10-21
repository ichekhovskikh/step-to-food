package com.sugar.steptofood.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.user.AnotherUserFragment

class AnotherUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_user_item)
        setFragment(AnotherUserFragment.getInstance())
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }
}