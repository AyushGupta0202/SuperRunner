@file:OptIn(ExperimentalMaterial3Api::class)

package com.eggdevs.run.presentation.active_run

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.StartIcon
import com.eggdevs.core.presentation.designsystem.StopIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerFloatingActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerScaffold
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerToolbar
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import com.eggdevs.run.presentation.R
import com.eggdevs.run.presentation.active_run.components.RunDataCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ObserveAsEvents(viewModel.activeRunEvents) { event ->
        when(event) {
            is ActiveRunEvent.Error -> TODO()
            ActiveRunEvent.RunSaved -> TODO()
        }
    }
    ActiveRunScreen(
        onAction = { action ->
            when(action) {

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState = ActiveRunState(),
    onAction: (ActiveRunAction) -> Unit = {}
) {
    SuperRunnerScaffold(
        withGradient = true,
        topAppBar = {
            SuperRunnerToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.active_run),
                showBackButton = true,
                onBackClick = { onAction(ActiveRunAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            SuperRunnerFloatingActionButton(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClick)
                },
                contentDescription = if (state.shouldTrack) stringResource(id = R.string.pause_run) else stringResource(id = R.string.start_run),
                iconSize = 20.dp
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
        ) {
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
            )
        }
    }
}

@Preview
@Composable
fun ActiveRunScreenPreview() {
    SuperRunnerTheme {
        ActiveRunScreen()
    }
}