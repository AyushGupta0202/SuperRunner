@file:OptIn(ExperimentalMaterial3Api::class)

package com.eggdevs.analytics.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.analytics.presentation.R
import com.eggdevs.analytics.presentation.dashboard.components.AnalyticsCard
import com.eggdevs.analytics.presentation.dashboard.models.AnalyticsCardUi
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerLoader
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerScaffold
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerToolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClick: () -> Unit = {},
    viewModel: AnalyticsDashboardViewModel = koinViewModel()
) {
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                AnalyticsDashboardAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun AnalyticsDashboardScreen(
    state: AnalyticsDashboardState? = null,
    onAction: (AnalyticsDashboardAction) -> Unit = {}
) {
    SuperRunnerScaffold(
        topAppBar = {
            SuperRunnerToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.analytics),
                onBackClick = {
                    onAction(AnalyticsDashboardAction.OnBackClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        if (state == null) {
            SuperRunnerLoader(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            val analyticsCardsList = listOf(
                AnalyticsCardUi(
                    title = stringResource(id = R.string.total_distance_run),
                    value = state.totalDistanceRun
                ),
                AnalyticsCardUi(
                    title = stringResource(id = R.string.total_time_run),
                    value = state.totalTimeRun
                ),
                AnalyticsCardUi(
                    title = stringResource(id = R.string.fastest_ever_run),
                    value = state.fastestEverRun
                ),
                AnalyticsCardUi(
                    title = stringResource(id = R.string.avg_distance_per_run),
                    value = state.avgDistance
                ),
                AnalyticsCardUi(
                    title = stringResource(id = R.string.avg_pace_per_run),
                    value = state.avgPace
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CustomGrid(
                    analyticsCardsList = analyticsCardsList
                )
            }
        }
    }
}

@Composable
private fun CustomGrid(
    modifier: Modifier = Modifier,
    analyticsCardsList: List<AnalyticsCardUi>
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(analyticsCardsList.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    AnalyticsCard(
                        modifier = Modifier
                            .weight(1f),
                        analyticsCardUi = item
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AnalyticsDashboardScreenPreview() {
    SuperRunnerTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "1303 km",
                totalTimeRun = "0d 0h 0m",
                avgDistance = "5.3 km",
                avgPace = "7:10",
                fastestEverRun = "143.7 km/h"
            )
        )
    }
}
