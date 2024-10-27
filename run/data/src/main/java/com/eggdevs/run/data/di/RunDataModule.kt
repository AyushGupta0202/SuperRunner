package com.eggdevs.run.data.di

import androidx.work.WorkManager
import com.eggdevs.core.domain.run.SyncRunScheduler
import com.eggdevs.run.data.connectivity.PhoneToWatchConnector
import com.eggdevs.run.data.workers.CreateRunWorker
import com.eggdevs.run.data.workers.DeleteRunWorker
import com.eggdevs.run.data.workers.FetchRunsWorker
import com.eggdevs.run.data.workers.SyncRunWorkerScheduler
import com.eggdevs.run.domain.connectivity.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    single {
        WorkManager.getInstance(get())
    }
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::CreateRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()

    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}