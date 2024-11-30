package com.eggdevs.core.connectivity.data.di

import com.eggdevs.core.connectivity.data.WearNodeDiscovery
import com.eggdevs.core.connectivity.data.messaging.WearMessagingClient
import com.eggdevs.core.connectivity.domain.NodeDiscovery
import com.eggdevs.core.connectivity.messaging.MessagingClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
    singleOf(::WearMessagingClient).bind<MessagingClient>()
}