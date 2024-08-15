package com.eggdevs.core.utils.di

import com.eggdevs.core.utils.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val utilsModule = module {
    single<CoroutineScope> {
        ApplicationScope()
    }
}