package com.sugar.steptofood.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sugar.steptofood.R


class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    fun registration(view: View) {
        //TODO enter if login, pass not exist in db -> login
        finish()
    }
}
