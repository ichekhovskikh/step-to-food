package com.sugar.steptofood.extension

import android.widget.ArrayAdapter

fun <T> ArrayAdapter<T>.getAll(): List<T?> {
    val items: MutableList<T?> = mutableListOf()
    for (i in 0 until count) {
        items.add(getItem(i))
    }
    return items
}