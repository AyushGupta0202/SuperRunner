package com.eggdevs.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    val durationMillis: Long,
    val maxSpeedKmh: Double,
    val avgSpeedKmh: Double,
    val mapPictureUrl: String?,
    val totalElevationMeters: Int,
    val distanceMeters: Int,
    val dateTimeUtc: String,
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey(autoGenerate = false)
    val id: String = ObjectId().toHexString()
)