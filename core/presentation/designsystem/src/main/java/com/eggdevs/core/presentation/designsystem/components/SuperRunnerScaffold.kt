package com.eggdevs.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme

@Composable
fun SuperRunnerScaffold(
    modifier: Modifier = Modifier,
    withGradient: Boolean = true,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
    ) { padding ->
        if (withGradient) {
            GradientBackground {
                content(padding)
            }
        } else {
            content(padding)
        }
    }
}

@Preview
@Composable
fun SuperRunnerScaffoldPreview(
    modifier: Modifier = Modifier
) {
    SuperRunnerTheme {
        SuperRunnerScaffold()
    }
}