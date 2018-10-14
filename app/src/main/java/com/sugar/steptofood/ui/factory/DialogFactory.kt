package com.sugar.steptofood.ui.factory

import android.app.AlertDialog
import android.content.Context
import com.sugar.steptofood.R

class DialogFactory private constructor() {

    companion object {

        fun createExitDialog(context: Context?, positive: () -> Unit): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context?.getString(R.string.dialog_tittle))
            builder.setMessage(context?.getString(R.string.dialog_message))
            builder.setNegativeButton(context?.getString(R.string.no)) { dialog, which -> }
            builder.setPositiveButton(context?.getString(R.string.yes)) { dialog, which -> positive.invoke() }
            return builder.create()
        }
    }
}