package com.eggdevs.run.presentation.run_overview

import com.eggdevs.run.presentation.run_overview.models.RunUi

sealed interface RunOverviewAction {
    data object OnStartRunClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
    data object OnLogoutClick: RunOverviewAction
    data class OnDeleteRunClick(val runUi: RunUi): RunOverviewAction
}