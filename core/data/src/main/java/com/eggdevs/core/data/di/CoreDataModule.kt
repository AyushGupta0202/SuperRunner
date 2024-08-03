package com.eggdevs.core.data.di

import com.eggdevs.core.data.auth.EncryptedSessionStorage
import com.eggdevs.core.data.networking.HttpClientFactory
import com.eggdevs.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}