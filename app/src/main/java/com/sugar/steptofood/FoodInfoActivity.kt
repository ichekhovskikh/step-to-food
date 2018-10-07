package com.sugar.steptofood

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import kotterknife.bindView

class FoodInfoActivity : AppCompatActivity() {
    private val oodInfoImageContainerLayout: ConstraintLayout? by bindView(R.id.foodInfoImageContainer)
    private var inflater: LayoutInflater? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_food_info)
        supportActionBar?.hide()
        addLikeButton()
    }

    @SuppressLint("InflateParams")
    private fun addLikeButton() {
        val buttonLike = inflater?.inflate(R.layout.widget_like_button, null)
        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.bottomToBottom = R.id.imageFoodView
        params.topToTop = R.id.imageFoodView
        params.startToStart = R.id.foodInfoImageContainer
        params.endToEnd = R.id.foodInfoImageContainer
        params.horizontalBias = 0.98f
        params.verticalBias = 0.1f
        params.width = 100
        params.height = 100
        oodInfoImageContainerLayout?.addView(buttonLike, params)
    }
}
