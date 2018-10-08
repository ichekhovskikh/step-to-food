package com.sugar.steptofood

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_recipes.*

class ItemMenuActivity : AppCompatActivity() {
    private var inflater: LayoutInflater? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.fragment_recipes)
        initTittle()
    }

    @SuppressLint("InflateParams")
    private fun initTittle() {
        val tittleTextView = inflater?.inflate(R.layout.item_menu_title, null) as TextView
        tittleTextView.text = intent.getStringExtra(ITEM_TITTLE)
        underTabContainer.addView(tittleTextView)
    }

    companion object {
        const val ITEM_TITTLE = "TITTLE"
    }
}
