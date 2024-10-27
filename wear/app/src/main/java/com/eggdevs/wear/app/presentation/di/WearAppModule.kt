package com.eggdevs.wear.app.presentation.di

import com.eggdevs.wear.app.presentation.SuperRunnerWearApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val wearAppModule = module {
    single<CoroutineScope> {
        (androidApplication() as SuperRunnerWearApp).applicationScope
    }
}