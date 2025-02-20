package com.eggdevs.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eggdevs.auth.domain.PasswordValidationState
import com.eggdevs.auth.domain.UserDataValidator
import com.eggdevs.auth.presentation.R
import com.eggdevs.core.presentation.designsystem.CheckIcon
import com.eggdevs.core.presentation.designsystem.CrossIcon
import com.eggdevs.core.presentation.designsystem.EmailIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerDarkRed
import com.eggdevs.core.presentation.designsystem.SuperRunnerGreen
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.GradientBackground
import com.eggdevs.core.presentation.designsystem.components.SucceedingClickableText
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerPasswordTextField
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerTextField
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit = {},
    onSuccessfulRegistration: () -> Unit = {},
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.authEvents) { authEvent ->
        when(authEvent) {
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    authEvent.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.register_successful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulRegistration()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> {
                    onSignInClick()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )
            // Clickable text for login starts
            SucceedingClickableText(
                precedingText = stringResource(id = R.string.already_have_an_account),
                succeedingClickableText = stringResource(id = R.string.login),
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                }
            )
            // Clickable text for login ends

            Spacer(modifier = Modifier.height(48.dp))
            SuperRunnerTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) {
                    CheckIcon
                } else {
                    null
                },
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id =R.string.email),
                additionalInfo = stringResource(id = R.string.must_be_a_valid_email),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))
            SuperRunnerPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordRequirement(
                text = stringResource(
                    id = R.string.at_least_x_characters,
                    UserDataValidator.MIN_PASSWORD_LENGTH,
                ),
                isValid = state.passwordValidationState.hasMinLength
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(id = R.string.at_least_one_number),
                isValid = state.passwordValidationState.hasNumber
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(id = R.string.contains_lowercase_character),
                isValid = state.passwordValidationState.hasLowerCaseCharacter
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(id = R.string.contains_uppercase_character),
                isValid = state.passwordValidationState.hasUpperCaseCharacter
            )

            Spacer(modifier = Modifier.height(32.dp))

            SuperRunnerActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister && !state.isRegistering,
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                }
            )
        }
    }
}

@Composable
fun PasswordRequirement(
    modifier: Modifier = Modifier,
    text: String,
    isValid: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) {
                CheckIcon
            } else {
                CrossIcon
            },
            contentDescription = null,
            tint = if (isValid) SuperRunnerGreen else SuperRunnerDarkRed
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SuperRunnerTheme {
        RegisterScreen(
            state = RegisterState(
                isEmailValid = true,
                passwordValidationState = PasswordValidationState(
                    hasMinLength = true,
                    hasNumber = true,
                    hasLowerCaseCharacter = true,
                    hasUpperCaseCharacter = true
                )
            ),
            onAction = {}
        )
    }
}
