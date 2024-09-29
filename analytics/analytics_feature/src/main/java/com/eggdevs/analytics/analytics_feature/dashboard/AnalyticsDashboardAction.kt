package com.eggdevs.analytics.analytics_feature.dashboard

sealed interface AnalyticsDashboardAction {
    data object OnBackClick: AnalyticsDashboardAction
}