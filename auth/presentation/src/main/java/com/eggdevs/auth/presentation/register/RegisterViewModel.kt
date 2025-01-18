package com.eggdevs.auth.presentation.register

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val authEventChannel = Channel<RegisterEvent>()
    val authEvents = authEventChannel.receiveAsFlow()


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
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            RegisterAction.OnRegisterClick -> register()
//            RegisterAction.OnLoginClick -> {} -> {} -> We can directly handle this from the UI
            else -> Unit
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = authRepository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isRegistering = false)
            when(result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        authEventChannel.send(RegisterEvent.Error(
                            UiText.StringResource(R.string.error_email_exists)
                        ))
                    } else {
                        authEventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success -> authEventChannel.send(RegisterEvent.RegistrationSuccess)
            }
        }
    }
}