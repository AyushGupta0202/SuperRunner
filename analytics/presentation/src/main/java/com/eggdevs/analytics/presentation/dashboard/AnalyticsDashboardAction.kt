package com.eggdevs.analytics.presentation.dashboard

sealed interface AnalyticsDashboardAction {
    data object OnBackClick: AnalyticsDashboardAction
}