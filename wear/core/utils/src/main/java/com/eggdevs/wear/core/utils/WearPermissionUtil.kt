package com.eggdevs.wear.core.utils

import android.Manifest
import android.content.Context
import com.eggdevs.core.utils.hasPermission

fun Context.hasBodySensorsPermission(): Boolean {
    return hasPermission(Manifest.permission.BODY_SENSORS)
}