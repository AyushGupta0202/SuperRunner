package com.eggdevs.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlin.math.abs

object PolylineColorCalculator {

    fun locationsToColor(
        locationA: LocationWithAltitudeTimestamp,
        locationB: LocationWithAltitudeTimestamp,
    ): Color {
        val distanceMeters = locationA.locationWithAltitude.location.distanceTo(locationB.locationWithAltitude.location)
        val timeDiff = abs((locationB.durationTimestamp - locationA.durationTimestamp).inWholeSeconds)
        val speedKmh = (distanceMeters / timeDiff) * 3.6
        return interpolateColor(
            speedKmh = speedKmh,
            minSpeed = 5.0,
            maxSpeed = 20.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }

    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)
        val colorInt = if (ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }
        return Color(colorInt)
    }
}