@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.eggdevs.run.presentation.run_overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.AnalyticsIcon
import com.eggdevs.core.presentation.designsystem.LogoIcon
import com.eggdevs.core.presentation.designsystem.LogoutIcon
import com.eggdevs.core.presentation.designsystem.RunIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerFloatingActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerScaffold
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerToolbar
import com.eggdevs.core.presentation.designsystem.models.DropDownItem
import com.eggdevs.run.presentation.R
import com.eggdevs.run.presentation.run_overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    onStartRunClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverviewScreen(
        onAction = { action ->
            when(action) {
                RunOverviewAction.OnStartRunClick -> {
                    onStartRunClick()
                }
                RunOverviewAction.OnLogoutClick -> {
                    onLogoutClick()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        },
        state = viewModel.state
    )
}

@Composable
fun RunOverviewScreen(
    onAction: (RunOverviewAction) -> Unit = {},
    state: RunOverviewState = RunOverviewState()
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    SuperRunnerScaffold(
        withGradient = true,
        topAppBar = {
            SuperRunnerToolbar(
                title = stringResource(id = R.string.super_runner),
                dropDownItemList = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logout)
                    )
                ),
                onMenuItemClick = {
                    when(it) {
                        1 -> onAction(RunOverviewAction.OnLogoutClick)
                        2 -> onAction(RunOverviewAction.OnAnalyticsClick)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            SuperRunnerFloatingActionButton(
                icon = RunIcon,
                contentDescription = stringResource(id = R.string.start_run),
                onClick = {
                    onAction(RunOverviewAction.OnStartRunClick)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.runs,
                key = {
                    it.id
                }
            ) {
                RunListItem(
                    modifier = Modifier
                        .animateItemPlacement(),
                    runUi = it,
                    onDeleteClick = {
                        onAction(RunOverviewAction.OnDeleteRunClick(it))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    SuperRunnerTheme {
        RunOverviewScreen()
    }
}
