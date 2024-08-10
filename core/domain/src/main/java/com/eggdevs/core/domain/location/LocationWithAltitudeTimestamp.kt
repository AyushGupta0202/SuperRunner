package com.eggdevs.core.domain.location

import kotlin.time.Duration

data class LocationWithAltitudeTimestamp(
    val locationWithAltitude: LocationWithAltitude,
    val durationTimestamp: Duration
)
