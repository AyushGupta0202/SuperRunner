package com.eggdevs.core.presentation.ui

import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

fun Duration.formatted(): String {
    return toComponents { hours, minutes, seconds, _ ->
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}

fun Double.toFormattedKm(): String {
    return "${roundToDecimalPlace(1)} km"
}

fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == ZERO || distanceKm <= 0.0) {
        return "-"
    }
    val secondsPerKm = (inWholeSeconds / distanceKm).roundToInt()
    return secondsPerKm.seconds.toComponents { hours, minutes, seconds, _ ->
        if (hours == 0L) {
            String.format("%02d:%02d / km", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d / km", hours, minutes, seconds)
        }
    }
}

fun Double.toFormattedKmh(): String {
    return "${roundToDecimalPlace(1)} km/h"
}

fun Int.toFormattedMeters(): String {
    return "$this m"
}

fun Double.roundToDecimalPlace(decimalPlaceCount: Int): Double {
    val factor = 10f.pow(decimalPlaceCount)
    return round(this * factor) / factor
}

fun main() {
    val duration = 30.seconds
    println(duration.toFormattedPace(0.0))
}