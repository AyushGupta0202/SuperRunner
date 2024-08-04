package com.eggdevs.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartRunClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
    data object OnLogoutClick: RunOverviewAction
}