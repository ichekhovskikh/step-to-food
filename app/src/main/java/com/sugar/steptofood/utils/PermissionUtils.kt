package com.sugar.steptofood.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat

const val STORAGE_PERMISSIONS_REQUEST_CODE = 1

fun hasStoragePermissions(activity: Activity) = Build.VERSION.SDK_INT < 23 ||
        ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

@TargetApi(23)
fun requestStoragePermissions(activity: Activity, requestCode: Int) {
    activity.requestPermissions(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    ), requestCode)
}