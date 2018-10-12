package com.sugar.steptofood.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.support.v4.util.Pair
import android.support.v4.app.ActivityOptionsCompat
import com.sugar.steptofood.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO logic enter if exists else open login
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        //TODO logic enter if login, pass -> success
        val intent = Intent(this, MainTabsActivity::class.java)
        startActivity(intent)
    }

    fun toRegistration(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        val bundle: Bundle? = getAnimation()
        this.startActivity(intent, bundle)
    }

    private fun getAnimation(): Bundle? {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair<View, String>(findViewById(R.id.loginTextView),
                        getString(R.string.transition_name_login)),
                Pair<View, String>(findViewById(R.id.passTextView),
                        getString(R.string.transition_name_pass)),
                Pair<View, String>(findViewById(R.id.nameInputText),
                        getString(R.string.transition_name_name))
        )
        return options.toBundle()
    }
}
