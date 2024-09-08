package com.eggdevs.run.domain.models

import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {

    fun getTotalDistanceInMeters(locations: List<List<LocationWithAltitudeTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { locationA, locationB ->
                    locationA.locationWithAltitude.location.distanceTo(locationB.locationWithAltitude.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationWithAltitudeTimestamp>>): Double {
        return locations
            .maxOf { locationSet ->
                locationSet.zipWithNext { locationA, locationB ->
                    val distanceMeters = locationA.locationWithAltitude.location.distanceTo(
                        other = locationB.locationWithAltitude.location
                    )
                    // TODO: Are these conversions necessary?
                    // TODO: Write some extension or helper function for this logic
                    val timeHours = (locationB.durationTimestamp - locationA.durationTimestamp)
                        .toDouble(DurationUnit.HOURS)
                    if (timeHours == 0.0) {
                        0.0
                    } else {
                        (distanceMeters / 1000.0) / timeHours
                    }
                }.max()
            }
    }

    fun getTotalElevationMeters(locations: List<List<LocationWithAltitudeTimestamp>>): Int {
        return locations
            .sumOf { locationSet ->
                locationSet.zipWithNext { locationA, locationB ->
                    val altitudeA = locationA.locationWithAltitude.altitude
                    val altitudeB = locationB.locationWithAltitude.altitude
                    (altitudeB - altitudeA).coerceAtLeast(0.0)
                }.sum().roundToInt()
            }
    }
}

fun main() {
    val list = listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9)
    )
    val list1 = listOf(1, 2, 3)
    println(
        list.sumOf {
            it.zipWithNext { a, b ->
                println("a: $a, b: $b, a + b: ${a + b}")
                a + b
            }.sum()
        }
    )
}