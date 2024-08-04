@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")

package com.eggdevs.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eggdevs.auth.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val loginEventChannel = Channel<LoginEvent>()
    val loginEvents = loginEventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> {

            }
            LoginAction.OnSignUpClick -> {

            }
            LoginAction.OnTogglePasswordVisibilityClick -> {

            }
        }
    }
}