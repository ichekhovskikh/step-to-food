package com.sugar.steptofood.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.sugar.steptofood.R

class HandwrittenListAdapter<T> : ArrayAdapter<T> {
    constructor(context: Context) : super(context, R.layout.item_product_line_list)
    constructor(context: Context, items: Array<T>) : super(context, R.layout.item_product_line_list, items)
    constructor(context: Context, items: ArrayList<T>) : super(context, R.layout.item_product_line_list, items)
}