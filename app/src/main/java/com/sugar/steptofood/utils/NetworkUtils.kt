package com.sugar.steptofood.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.sugar.steptofood.App


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo: NetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo.isConnected
}