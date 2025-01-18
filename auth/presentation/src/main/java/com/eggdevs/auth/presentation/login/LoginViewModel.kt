package com.eggdevs.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.auth.domain.UserDataValidator
import com.eggdevs.auth.domain.repository.AuthRepository
import com.eggdevs.auth.presentation.R
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.presentation.ui.UiText
import com.eggdevs.core.presentation.ui.asUiText
import com.eggdevs.core.presentation.ui.textAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
): ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val loginEventChannel = Channel<LoginEvent>()
    val loginEvents = loginEventChannel.receiveAsFlow()

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow()) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(email.toString().trim())
                        && password.toString().isNotEmpty(),

            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(
                isLoggingIn = true
            )
            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(
                isLoggingIn = false
            )
            when(result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        loginEventChannel.send(
                            LoginEvent.Error(UiText.StringResource(R.string.error_email_password_incorrect))
                        )
                    } else {
                        loginEventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    loginEventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}