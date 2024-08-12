@file:OptIn(ExperimentalMaterial3Api::class)

package com.eggdevs.run.presentation.active_run

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eggdevs.core.presentation.designsystem.StartIcon
import com.eggdevs.core.presentation.designsystem.StopIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerDialog
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerFloatingActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerOutlinedActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerScaffold
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerToolbar
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import com.eggdevs.core.utils.isAtLeastAndroid13
import com.eggdevs.run.presentation.R
import com.eggdevs.run.presentation.active_run.components.RunDataCard
import com.eggdevs.run.presentation.utils.hasLocationPermission
import com.eggdevs.run.presentation.utils.hasNotificationPermission
import com.eggdevs.run.presentation.utils.shouldShowLocationPermissionRationale
import com.eggdevs.run.presentation.utils.shouldShowNotificationPermissionRationale
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
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCoarseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (isAtLeastAndroid13()) {
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }
        val activity = context as? Activity
        val showLocationRationale = activity?.shouldShowLocationPermissionRationale() ?: false
        val showNotificationRationale = activity?.shouldShowNotificationPermissionRationale() ?: false

        onAction(
            ActiveRunAction.SubmitLocationPermissionRationale(
                acceptedLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionRationale(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationRationale = showNotificationRationale
            )
        )
    }
    LaunchedEffect(key1 = true) {
        val activity = context as Activity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()
        onAction(
            ActiveRunAction.SubmitLocationPermissionRationale(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionRationale(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale
            )
        )
        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRunPermissions(context)
        }
    }
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

    if (state.showLocationRationale || state.showNotificationRationale) {
        SuperRunnerDialog(
            title = stringResource(id = R.string.permission_required),
            onDismissDialog = {/* Normal dismissing not allowed for permissions */},
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notifcation_rationale)
                }
                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }
                state.showNotificationRationale -> {
                    stringResource(id = R.string.notification_rationale)
                }
                else -> {
                    stringResource(id = R.string.permission_error)
                }
            },
            primaryContent = {
                SuperRunnerOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    onClick = {
                        onAction(ActiveRunAction.OnDismissPermissionRationaleDialog)
                        permissionLauncher.requestRunPermissions(context)
                    }
                )
            }
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRunPermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()
    val locationPermissionsArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val notificationPermissionArray = if (isAtLeastAndroid13()) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf()
    }
    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissionsArray + notificationPermissionArray)
        }
        !hasLocationPermission -> {
            launch(locationPermissionsArray)
        }
        !hasNotificationPermission -> {
            launch(notificationPermissionArray)
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