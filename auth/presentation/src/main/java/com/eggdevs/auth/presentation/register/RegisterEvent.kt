package com.eggdevs.auth.presentation.register

import com.eggdevs.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val message: UiText): RegisterEvent
}