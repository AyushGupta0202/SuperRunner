package com.eggdevs.superrunner

import android.app.Application
import com.eggdevs.auth.data.di.authDataModule
import com.eggdevs.auth.presentation.di.authViewModelModule
import com.eggdevs.core.data.di.coreDataModule
import com.eggdevs.core.database.di.databaseModule
import com.eggdevs.core.utils.di.utilsModule
import com.eggdevs.run.location.di.locationModule
import com.eggdevs.run.presentation.di.runPresentationModule
import com.eggdevs.superrunner.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SuperRunnerApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SuperRunnerApp)
            modules(
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                utilsModule,
                databaseModule
            )
        }
    }
}