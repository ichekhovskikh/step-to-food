package com.sugar.steptofood

import android.content.Context
import javax.inject.Inject

class Session @Inject constructor(context: Context) {

    private val sessionPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var token
        get() = sessionPrefs.getString(TOKEN, "")
        set(value) = sessionPrefs.edit().putString(TOKEN, value).apply()

    var userId
        get() = sessionPrefs.getInt(UID, 0)
        set(value) = sessionPrefs.edit().putInt(UID, value).apply()

    fun reset() {
        sessionPrefs.edit().clear().apply()
    }

    companion object {
        const val PREF_NAME = "sessionPrefs"

        const val TOKEN = "token"
        const val UID = "userId"
    }
}