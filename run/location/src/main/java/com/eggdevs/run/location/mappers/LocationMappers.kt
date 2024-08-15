package com.eggdevs.run.location.mappers

import com.eggdevs.core.domain.location.Location
import com.eggdevs.core.domain.location.LocationWithAltitude

fun android.location.Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = Location(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}