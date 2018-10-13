package com.sugar.steptofood.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyboardUtils private constructor() {

    companion object {
        fun showKeyboard(activity: Activity, textView: EditText) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT)
        }

        fun hideKeyboard(activity: Activity, textView: EditText) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(textView.windowToken, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}