package com.eggdevs.run.network.datasource.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateRunRequest(
    val distanceMeters: Int,
    val durationMillis: Long,
    val epochMillis: Long,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val id: String
)
