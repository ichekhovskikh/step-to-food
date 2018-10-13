package com.sugar.steptofood.utils

import android.text.TextUtils
import android.widget.EditText

class TextValidator private constructor() {

    companion object {
        fun validate(view: EditText, textError: String): Boolean {
            if (TextUtils.isEmpty(view.text)) {
                view.error = textError
                view.requestFocus()
                return false
            }
            return true
        }
    }
}