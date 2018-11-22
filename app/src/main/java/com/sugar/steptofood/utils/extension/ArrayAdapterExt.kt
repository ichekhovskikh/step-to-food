package com.sugar.steptofood.utils.extension

import android.widget.ArrayAdapter

fun <T> ArrayAdapter<T>.getAll(): ArrayList<T?> {
    val items: ArrayList<T?> = ArrayList()
    for (i in 0 until count) {
        items.add(getItem(i))
    }
    return items
}