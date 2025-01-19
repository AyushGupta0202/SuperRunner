@file:OptIn(ExperimentalMaterial3Api::class)

package com.eggdevs.run.presentation.active_run

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eggdevs.core.presentation.designsystem.StartIcon
import com.eggdevs.core.presentation.designsystem.StopIcon
import com.eggdevs.core.presentation.designsystem.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerDialog
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerFloatingActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerOutlinedActionButton
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerScaffold
import com.eggdevs.core.presentation.designsystem.components.SuperRunnerToolbar
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import com.eggdevs.core.utils.isAtLeastAndroid13
import com.eggdevs.run.presentation.R
import com.eggdevs.run.presentation.active_run.components.RunDataCard
import com.eggdevs.run.presentation.active_run.maps.TrackerMap
import com.eggdevs.core.notification.service.ActiveRunService
import com.eggdevs.core.utils.hasLocationPermission
import com.eggdevs.core.utils.hasNotificationPermission
import com.eggdevs.core.utils.shouldShowLocationPermissionRationale
import com.eggdevs.core.utils.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ActiveRunScreenRoot(
    onRunFinished: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ActiveRunViewModel = koinViewModel(),
    onServiceToggle: (isServiceRunning: Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    ObserveAsEvents(viewModel.activeRunEvents) { event ->
        when(event) {
            is ActiveRunEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            ActiveRunEvent.RunSaved -> {
                Toast.makeText(context, "run finished", Toast.LENGTH_SHORT).show()
                onRunFinished()
            }
        }
    }
    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onBack = onBack,
        onAction = { action ->
            when(action) {
                ActiveRunAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning) {
                        onBack()
                    }
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState = ActiveRunState(),
    onServiceToggle: (isServiceRunning: Boolean) -> Unit = {},
    onBack: () -> Unit = {},
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

    val isServiceActive by ActiveRunService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.shouldTrack, isServiceActive) {
        if (context.hasLocationPermission() && state.shouldTrack && !isServiceActive) {
            onServiceToggle(true)
        }
    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if (state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    SuperRunnerScaffold(
        withGradient = false,
        topAppBar = {
            SuperRunnerToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.active_run),
                showBackButton = true,
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
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
            TrackerMap(
                modifier = Modifier
                    .fillMaxSize(),
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = { bitmap ->
                    // TODO: check thread here
                    // TODO: make OnRunProcessed have a bitmap rather than byte array and do this in the viewmodel
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcessed(stream.toByteArray()))
                }
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .fillMaxWidth()
            )
        }
    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        SuperRunnerDialog(
            title = stringResource(id = R.string.running_is_paused),
            description = stringResource(id = R.string.resume_or_finish_run),
            onDismissDialog = {
                onAction(ActiveRunAction.OnResumeRunClick)
            },
            primaryContent = { // TODO: disable after finish button press and re-enable maybe after 5 seconds or so
                SuperRunnerActionButton(
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    }
                )
            },
            secondaryContent = { // TODO: disable after first press and re-enable maybe after 5 seconds or so
                SuperRunnerOutlinedActionButton(
                    text = stringResource(id = R.string.finish),
                    modifier = Modifier.weight(1f),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    }
                )
            }
        )
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