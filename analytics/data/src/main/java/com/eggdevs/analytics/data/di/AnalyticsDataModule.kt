package com.eggdevs.analytics.data.di

import com.eggdevs.analytics.data.repository.RoomAnalyticsRepository
import com.eggdevs.analytics.domain.repository.RunAnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<RunAnalyticsRepository>()
}