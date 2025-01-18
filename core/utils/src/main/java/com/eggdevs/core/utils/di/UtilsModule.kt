package com.eggdevs.core.utils.di

import com.eggdevs.core.utils.BuildConfigWrapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val utilsModule = module {
//    single<CoroutineScope> {
//        ApplicationScope()
//    }
    singleOf(::BuildConfigWrapper)
}