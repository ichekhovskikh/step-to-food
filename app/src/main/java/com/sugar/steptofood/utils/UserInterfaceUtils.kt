package com.sugar.steptofood.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun showKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_IMPLICIT)
    }
}