package com.eggdevs.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun SuperRunnerPolylines(
    locations: List<List<LocationWithAltitudeTimestamp>> = listOf()
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { locationA, locationB ->
                PolylineUi(
                    locationA = locationA.locationWithAltitude.location,
                    locationB = locationB.locationWithAltitude.location,
                    color = PolylineColorCalculator.locationsToColor(
                        locationA = locationA,
                        locationB = locationB
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.locationA.lat, polylineUi.locationA.long),
                    LatLng(polylineUi.locationB.lat, polylineUi.locationB.long)
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}