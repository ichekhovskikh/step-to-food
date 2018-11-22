package com.sugar.steptofood.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.sugar.steptofood.R

class HandwrittenListAdapter<T>(context: Context)
    : ArrayAdapter<T>(context, R.layout.item_product_line_list)