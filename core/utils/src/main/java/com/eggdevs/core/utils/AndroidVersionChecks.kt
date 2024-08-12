package com.eggdevs.core.utils

import android.os.Build

fun isAtLeastAndroid12() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
fun isAtLeastAndroid13() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU