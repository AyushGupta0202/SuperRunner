package com.eggdevs.run.domain.models

import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {

    fun getTotalDistanceInMeters(locations: List<List<LocationWithAltitudeTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { locationA, locationB ->
                    locationA.locationWithAltitude.location.distanceTo(locationB.locationWithAltitude.location)
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