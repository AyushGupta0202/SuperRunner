package com.eggdevs.analytics.data.repository

import com.eggdevs.analytics.domain.models.RunAnalyticsValues
import com.eggdevs.analytics.domain.repository.RunAnalyticsRepository
import com.eggdevs.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
): RunAnalyticsRepository {
    override suspend fun getAnalyticsValues(): RunAnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeRun = async { analyticsDao.getTotalTimeRun() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }
            val avgDistancePerRun = async { analyticsDao.getAverageDistancePerRun() }
            val avgPacePerRun = async { analyticsDao.getAveragePacePerRun() }
            val highestHeartRate = async { analyticsDao.getHighestHeartRate() }
            RunAnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeRun.await().milliseconds,
                fastestEverRun = maxRunSpeed.await(),
                avgDistancePerRun = avgDistancePerRun.await(),
                avgPacePerRun = avgPacePerRun.await(),
                highestHeartRate = highestHeartRate.await()
            )
        }
    }
}