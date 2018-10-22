package com.sugar.steptofood.utils

import android.text.TextUtils
import android.widget.EditText

fun validateTextView(view: EditText, textError: String? = null) =
        if (TextUtils.isEmpty(view.text)) {
            view.error = textError
            view.requestFocus()
            false
        } else true