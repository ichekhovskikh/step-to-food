package com.sugar.steptofood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FoodInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)
        getSupportActionBar()?.hide()
    }
}
