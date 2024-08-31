package com.eggdevs.core.domain.run

import com.eggdevs.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?, // null if new run
    val distanceMeters: Int,
    val duration: Duration,
    val maxSpeedKmh: Double,
    val mapPictureUrl: String,
    val dateTimeUtc: ZonedDateTime,
    val location: Location,
    val totalElevationMeters: Int
) {
    val avgSpeedKmh: Double
        get() = (distanceMeters / 1000.0) / duration.toDouble(DurationUnit.HOURS)
}
