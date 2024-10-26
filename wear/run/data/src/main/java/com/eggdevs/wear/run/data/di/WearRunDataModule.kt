package com.eggdevs.wear.run.data.di

import com.eggdevs.wear.run.data.tracker.HealthServicesExerciseTracker
import com.eggdevs.wear.run.domain.tracker.ExerciseTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
}