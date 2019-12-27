package com.yasser.nearby.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yasser.nearby.BuildConfig

fun Context.isPermissionGranted(permissionName: String): Boolean {
    val checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, permissionName)
    return checkSelfPermissionResult == PackageManager.PERMISSION_GRANTED
}

fun Activity.shouldProvideRationale(permissionName: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)
}


fun Activity.requestPermission(permissionName: String, requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(permissionName), requestCode)
}

fun Activity.goToMobileSettings() {
    // Build intent that displays the App settings screen.
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}