package com.eggdevs.analytics.presentation.dashboard.mappers

import com.eggdevs.analytics.presentation.dashboard.AnalyticsDashboardState
import com.eggdevs.analytics.domain.models.RunAnalyticsValues
import com.eggdevs.core.presentation.ui.formatted
import com.eggdevs.core.presentation.ui.formattedDayHoursMinutes
import com.eggdevs.core.presentation.ui.toFormattedKm
import com.eggdevs.core.presentation.ui.toFormattedKmh
import kotlin.time.Duration.Companion.seconds

fun RunAnalyticsValues.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.formattedDayHoursMinutes(),
        avgDistance = (avgDistancePerRun / 1000.0).toFormattedKm(),
        avgPace = avgPacePerRun.seconds.formatted(),
        fastestEverRun = fastestEverRun.toFormattedKmh()
    )
}