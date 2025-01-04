package com.eggdevs.core.data.di

import com.eggdevs.core.data.auth.EncryptedSessionStorage
import com.eggdevs.core.data.networking.HttpClientFactory
import com.eggdevs.core.data.run.OfflineFirstRunRepository
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.run.repository.RunRepository
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build(CIO.create())
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}