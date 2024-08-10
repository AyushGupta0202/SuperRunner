package com.eggdevs.run.domain.models

import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LocationWithAltitudeTimestamp>> = emptyList(),
)
