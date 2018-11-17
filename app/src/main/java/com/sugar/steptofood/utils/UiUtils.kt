package com.sugar.steptofood.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.sugar.steptofood.R

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

fun showExitDialog(context: Context?, positive: () -> Unit, negative: () -> Unit = {}) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context?.getString(R.string.dialog_tittle))
    builder.setMessage(context?.getString(R.string.dialog_message))
    builder.setNegativeButton(context?.getString(R.string.no)) { _, _ -> negative.invoke() }
    builder.setPositiveButton(context?.getString(R.string.yes)) { _, _ -> positive.invoke() }
    builder.create().show()
}