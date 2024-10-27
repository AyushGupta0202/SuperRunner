package com.eggdevs.wear.app.presentation

import android.app.Application
import com.eggdevs.core.connectivity.data.di.coreConnectivityDataModule
import com.eggdevs.wear.app.presentation.di.wearAppModule
import com.eggdevs.wear.run.data.di.wearRunDataModule
import com.eggdevs.wear.run.presentation.di.wearRunPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SuperRunnerWearApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SuperRunnerWearApp)
            modules(
                wearAppModule,
                wearRunPresentationModule,
                wearRunDataModule,
                coreConnectivityDataModule
            )
        }
    }
}