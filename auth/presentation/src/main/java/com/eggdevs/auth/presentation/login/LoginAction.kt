package com.eggdevs.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginClick: LoginAction
    data object OnSignUpClick: LoginAction
    data object OnTogglePasswordVisibilityClick: LoginAction
}