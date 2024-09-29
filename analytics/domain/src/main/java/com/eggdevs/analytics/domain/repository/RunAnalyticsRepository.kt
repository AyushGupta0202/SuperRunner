package com.eggdevs.analytics.domain.repository

import com.eggdevs.analytics.domain.models.RunAnalyticsValues

interface RunAnalyticsRepository {
    suspend fun getAnalyticsValues(): RunAnalyticsValues
}