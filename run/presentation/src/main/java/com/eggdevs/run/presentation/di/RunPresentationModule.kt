package com.eggdevs.run.presentation.di

import com.eggdevs.run.domain.RunningTracker
import com.eggdevs.run.presentation.active_run.ActiveRunViewModel
import com.eggdevs.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)
    single {
        get<RunningTracker>().elapsedTime
    }
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}