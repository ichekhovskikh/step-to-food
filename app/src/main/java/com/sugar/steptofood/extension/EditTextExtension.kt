package com.sugar.steptofood.extension

import android.text.TextUtils
import android.widget.EditText
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun EditText.validate(textError: String? = null) =
        if (TextUtils.isEmpty(this.text)) {
            this.error = textError
            this.requestFocus()
            false
        } else true