package com.eggdevs.auth.presentation.di

import com.eggdevs.auth.presentation.login.LoginViewModel
import com.eggdevs.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}