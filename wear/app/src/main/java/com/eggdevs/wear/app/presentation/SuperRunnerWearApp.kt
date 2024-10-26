package com.eggdevs.wear.app.presentation

import android.app.Application
import com.eggdevs.wear.run.presentation.di.wearRunPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SuperRunnerWearApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SuperRunnerWearApp)
            modules(
                wearRunPresentationModule
            )
        }
    }
}