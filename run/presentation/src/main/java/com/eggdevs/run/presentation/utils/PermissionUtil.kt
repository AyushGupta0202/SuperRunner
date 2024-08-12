package com.eggdevs.run.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.eggdevs.core.utils.isAtLeastAndroid13

fun Activity.shouldShowLocationPermissionRationale(): Boolean {
    return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Activity.shouldShowNotificationPermissionRationale(): Boolean {
    return if (isAtLeastAndroid13()) {
        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        false
    }
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasNotificationPermission(): Boolean {
    return if (isAtLeastAndroid13()) {
        hasPermission(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        true
    }
}

fun Context.hasLocationPermission(): Boolean {
    return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
}