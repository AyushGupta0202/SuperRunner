package com.eggdevs.run.data.di

import com.eggdevs.run.data.workers.CreateRunWorker
import com.eggdevs.run.data.workers.DeleteRunWorker
import com.eggdevs.run.data.workers.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::CreateRunWorker)
}