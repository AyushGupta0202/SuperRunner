package com.eggdevs.superrunner

import android.app.Application
import com.eggdevs.auth.data.di.authDataModule
import com.eggdevs.auth.presentation.di.authViewModelModule
import com.eggdevs.core.data.di.coreDataModule
import com.eggdevs.superrunner.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SuperRunnerApp: Application() {

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
                coreDataModule
            )
        }
    }
}