package com.sugar.steptofood.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.sugar.steptofood.R

class HandwrittenListAdapter : ArrayAdapter<String> {
    constructor(context: Context) : super(context, R.layout.item_product_line_list)
    constructor(context: Context, items: Array<String>) : super(context, R.layout.item_product_line_list, items)
    constructor(context: Context, items: ArrayList<String>) : super(context, R.layout.item_product_line_list, items)
}