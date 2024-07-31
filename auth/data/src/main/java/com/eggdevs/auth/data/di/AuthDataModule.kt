package com.eggdevs.auth.data.di

import com.eggdevs.auth.data.EmailValidator
import com.eggdevs.auth.domain.PatternValidator
import com.eggdevs.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailValidator
    }
    singleOf(::UserDataValidator)
}