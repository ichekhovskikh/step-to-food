package com.sugar.steptofood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.support.v4.util.Pair
import android.support.v4.app.ActivityOptionsCompat



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }

    fun openRegistrationView(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        val bundle: Bundle? = getAnimation()
        this.startActivity(intent, bundle)
    }

    private fun getAnimation(): Bundle? {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair<View, String>(findViewById(R.id.textLogin),
                        getString(R.string.transition_name_login)),
                Pair<View, String>(findViewById(R.id.textPass),
                        getString(R.string.transition_name_pass)),
                Pair<View, String>(findViewById(R.id.textName),
                        getString(R.string.transition_name_name))
        )
        return options.toBundle()
    }
}
