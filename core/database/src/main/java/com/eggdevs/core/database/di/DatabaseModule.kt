package com.eggdevs.core.database.di

import androidx.room.Room
import com.eggdevs.core.database.Migration_1_2
import com.eggdevs.core.database.RunDatabase
import com.eggdevs.core.database.datasource.RoomLocalRunDataSource
import com.eggdevs.core.domain.run.datasource.local.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        )
            .addMigrations(Migration_1_2)
            .build()
    }

    single { get<RunDatabase>().runDao }
    single { get<RunDatabase>().runPendingSyncDao }
    single { get<RunDatabase>().deletedRunPendingSyncDao }

    singleOf(::RoomLocalRunDataSource).bind<LocalRunDataSource>()
}