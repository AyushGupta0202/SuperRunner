@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package com.eggdevs.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.auth.domain.UserDataValidator
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterViewModel(
    private val userDataValidator: UserDataValidator
): ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    init {
        state.email.textAsFlow()
            .onEach {
                val isValidEmail = userDataValidator.isValidEmail(it.toString())
                state = state.copy(
                    isEmailValid = isValidEmail,
                    canRegister = isValidEmail
                            && state.passwordValidationState.isValidPassword
                            && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

        state.password.textAsFlow()
            .onEach {
                val passwordValidationState = userDataValidator.validatePassword(it.toString())
                state = state.copy(
                    passwordValidationState = passwordValidationState,
                    canRegister = state.isEmailValid
                            && passwordValidationState.isValidPassword
                            && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> TODO()
            RegisterAction.OnRegisterClick -> TODO()
            RegisterAction.OnTogglePasswordVisibilityClick -> TODO()
        }
    }
}