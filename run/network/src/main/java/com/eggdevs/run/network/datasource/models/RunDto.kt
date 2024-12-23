package com.eggdevs.run.network.datasource.models

import kotlinx.serialization.Serializable

@Serializable
data class RunDto(
    val distanceMeters: Int,
    val durationMillis: Long,
    val lat: Double,
    val long: Double,
    val dateTimeUtc: String,
    val mapPictureUrl: String?,
    val id: String,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?
)