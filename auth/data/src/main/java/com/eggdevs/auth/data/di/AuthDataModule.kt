package com.eggdevs.auth.data.di

import com.eggdevs.auth.data.EmailValidator
import com.eggdevs.auth.data.repository.AuthRepositoryImpl
import com.eggdevs.auth.data.repository.AuthRepositoryStaticMock
import com.eggdevs.auth.domain.PatternValidator
import com.eggdevs.auth.domain.UserDataValidator
import com.eggdevs.auth.domain.repository.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailValidator
    }
    singleOf(::UserDataValidator)
//    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::AuthRepositoryStaticMock).bind<AuthRepository>()
}