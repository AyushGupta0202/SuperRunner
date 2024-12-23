package com.eggdevs.run.domain.models

import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LocationWithAltitudeTimestamp>> = emptyList(),
    val heartRates: List<Int> = emptyList()
)

fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    return if (this.isEmpty()) {
        listOf(replacement)
    } else {
        this.dropLast(1) + listOf(replacement)
    }
}