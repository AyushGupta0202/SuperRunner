package com.eggdevs.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnalyticsDao {
    @Query("SELECT SUM(distanceMeters) FROM RunEntity")
    suspend fun getTotalDistance(): Int
    @Query("SELECT SUM(durationMillis) FROM RunEntity")
    suspend fun getTotalTimeRun(): Long
    @Query("SELECT MAX(maxSpeedKmh) FROM RunEntity")
    suspend fun getMaxRunSpeed(): Double
    @Query("SELECT AVG(distanceMeters) FROM RunEntity")
    suspend fun getAverageDistancePerRun(): Double
    @Query("SELECT AVG((durationMillis / 60000.0) / (distanceMeters / 1000.0)) FROM RunEntity")
    suspend fun getAveragePacePerRun(): Double
    @Query("SELECT MAX(maxHeartRate) FROM RunEntity")
    suspend fun getHighestHeartRate(): Int?
}