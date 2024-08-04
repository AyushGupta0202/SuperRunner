@file:OptIn(ExperimentalFoundationApi::class)

package com.eggdevs.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eggdevs.auth.presentation.R
import com.eggdevs.core.presentation.designsystem.EmailIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.GradientBackground
import com.eggdevs.core.presentation.designsystem.components.SucceedingClickableText
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerPasswordTextField
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerTextField
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.loginEvents) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.youre_logged_in,
                    Toast.LENGTH_SHORT
                ).show()
                onLoginClick()
            }
        }
    }
    LoginScreen(
        state = viewModel.state,
        onAction = { loginAction ->
            when (loginAction) {
                LoginAction.OnSignUpClick -> {
                    onSignUpClick()
                }
                else -> Unit
            }
            viewModel.onAction(loginAction)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {}
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hi_there),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = R.string.super_runner_welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            SuperRunnerTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            SuperRunnerPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.password,
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(32.dp))
            SuperRunnerActionButton(
                text = stringResource(id = R.string.login),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canLogin
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.BottomCenter
            ) {
                SucceedingClickableText(
                    precedingText = stringResource(id = R.string.dont_have_an_account),
                    succeedingClickableText = stringResource(id = R.string.sign_up),
                    onClick = {
                        onAction(LoginAction.OnSignUpClick)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    SuperRunnerTheme {
        LoginScreen()
    }
}
