package com.sugar.steptofood.utils.extension

import android.text.TextUtils
import android.widget.EditText

fun EditText.validate(textError: String? = null) =
        if (TextUtils.isEmpty(this.text)) {
            this.error = textError
            this.requestFocus()
            false
        } else true