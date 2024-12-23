package com.eggdevs.run.network.di

import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.run.network.datasource.KtorRemoteRunDataSource
import com.eggdevs.run.network.datasource.RemoteRunDataSourceStaticMock
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
//    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
    singleOf(::RemoteRunDataSourceStaticMock).bind<RemoteRunDataSource>()
}