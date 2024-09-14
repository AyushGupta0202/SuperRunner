package com.eggdevs.run.presentation.run_overview

import com.eggdevs.run.presentation.run_overview.models.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
