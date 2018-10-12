package com.sugar.steptofood.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.RegistrationActivity
import kotlinx.android.synthetic.main.item_product_line_list.view.*

class HandwrittenListAdapter : ArrayAdapter<String> /*TODO string replace food*/ {
    constructor(context: Context) : super(context, R.layout.item_product_line_list)
    constructor(context: Context, items: Array<String>) : super(context, R.layout.item_product_line_list, items)
    constructor(context: Context, items: ArrayList<String>) : super(context, R.layout.item_product_line_list, items)

    fun getAllItems(): Array<String?> {
        val items: MutableList<String?> = mutableListOf()
        for (i in 0 until count) {
            items.add(getItem(i))
        }
        return items.toTypedArray()
    }


}