package com.eggdevs.run.location.di

import com.eggdevs.run.domain.LocationObserver
import com.eggdevs.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}