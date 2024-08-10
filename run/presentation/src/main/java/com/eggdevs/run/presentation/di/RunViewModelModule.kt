package com.eggdevs.run.presentation.di

import com.eggdevs.run.presentation.active_run.ActiveRunViewModel
import com.eggdevs.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}