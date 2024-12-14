package com.eggdevs.wear.run.presentation.di

import com.eggdevs.wear.run.domain.tracker.WearRunningTracker
import com.eggdevs.wear.run.presentation.tracker.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val wearRunPresentationModule = module {
    viewModelOf(::TrackerViewModel)
    singleOf(::WearRunningTracker)
}