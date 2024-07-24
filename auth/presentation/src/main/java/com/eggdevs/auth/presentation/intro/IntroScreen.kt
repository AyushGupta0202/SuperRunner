package com.eggdevs.auth.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eggdevs.auth.presentation.R
import com.eggdevs.core.presentation.designsystem.LogoIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.GradientBackground
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerOutlinedActionButton

@Composable
fun IntroScreenRoot(
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    IntroScreen(
        onAction = { action ->
            when(action) {
                IntroAction.OnSignInClick -> onSignInClick()
                IntroAction.OnSignUpClick -> onSignUpClick()
            }
        }
    )
}

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit = {}
) {
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            SuperRunnerLogoVertical()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_to_super_runner),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.super_runner_description),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            SuperRunnerOutlinedActionButton(
                text = stringResource(id = R.string.sign_in),
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(IntroAction.OnSignInClick)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SuperRunnerActionButton(
                text = stringResource(id = R.string.sign_up),
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(IntroAction.OnSignUpClick)
                }
            )
        }
    }
}

@Composable
private fun SuperRunnerLogoVertical(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = LogoIcon,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.super_runner),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun IntroScreenPreview() {
    SuperRunnerTheme {
        IntroScreen()
    }
}