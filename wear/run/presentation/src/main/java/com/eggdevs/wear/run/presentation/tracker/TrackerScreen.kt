package com.eggdevs.wear.run.presentation.tracker

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.eggdevs.core.notification.service.ActiveRunService
import com.eggdevs.core.presentation.designsystem.ExclamationMarkIcon
import com.eggdevs.core.presentation.designsystem.FinishIcon
import com.eggdevs.core.presentation.designsystem.PauseIcon
import com.eggdevs.core.presentation.designsystem.StartIcon
import com.eggdevs.core.presentation.designsystem_wear.SuperRunnerTheme
import com.eggdevs.core.presentation.designsystem_wear.ambient.AmbientObserver
import com.eggdevs.core.presentation.designsystem_wear.ambient.ambientMode
import com.eggdevs.core.presentation.ui.ObserveAsEvents
import com.eggdevs.core.presentation.ui.formatted
import com.eggdevs.core.presentation.ui.toFormattedHeartRate
import com.eggdevs.core.presentation.ui.toFormattedKm
import com.eggdevs.core.utils.hasNotificationPermission
import com.eggdevs.core.utils.isAtLeastAndroid13
import com.eggdevs.wear.core.utils.hasBodySensorsPermission
import com.eggdevs.wear.run.presentation.R
import com.eggdevs.wear.run.presentation.tracker.components.RunDataCard
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrackerScreenRoot(
    onServiceToggle: (isServiceRunning: Boolean) -> Unit = {},
    viewModel: TrackerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val isServiceActive by ActiveRunService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(state.isRunActive, state.hasStartedRunning, isServiceActive) {
        if (state.isRunActive && !isServiceActive) {
            onServiceToggle(true)
        }
    }
    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is TrackerEvent.Error -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
            TrackerEvent.RunFinished -> {
                onServiceToggle(false)
            }
        }
    }
    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
fun TrackerScreen(
    state: TrackerState = TrackerState(),
    onAction: (TrackerAction) -> Unit = {}
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasBodySensorsPermission = perms[Manifest.permission.BODY_SENSORS] == true
        onAction(TrackerAction.OnBodySensorsPermissionResult(hasBodySensorsPermission))
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        val hasBodySensorsPermission = context.hasBodySensorsPermission()
        onAction(TrackerAction.OnBodySensorsPermissionResult(hasBodySensorsPermission))

        val hasNotificationPermission = context.hasNotificationPermission()

        val permissions = mutableListOf<String>()
        if (!hasBodySensorsPermission) {
            permissions.add(Manifest.permission.BODY_SENSORS)
        }
        if (!hasNotificationPermission && isAtLeastAndroid13()) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    AmbientObserver(
        onEnterAmbient = { ambientDetails ->
            onAction(TrackerAction.OnEnterAmbientMode(ambientDetails.burnInProtectionRequired))
        },
        onExitAmbient = {
            onAction(TrackerAction.OnExitAmbientMode)
        }
    )

    if (state.isConnectedPhoneNearby) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .ambientMode(state.isAmbientMode, state.burnInProtectionRequired),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                RunDataCard(
                    title = stringResource(id = R.string.heart_rate),
                    value = if (state.canTrackHeartRate) {
                        state.heartRate.toFormattedHeartRate()
                    } else {
                        stringResource(id = R.string.unsupported)
                    },
                    valueTextColor = if (state.canTrackHeartRate) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                RunDataCard(
                    title = stringResource(id = R.string.distance),
                    value = (state.distanceMeters / 1000.0).toFormattedKm(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.elapsedDuration.formatted(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isTrackable) {
                    ToggleRunButton(
                        isRunActive = state.isRunActive,
                        onClick = {
                            onAction(TrackerAction.OnToggleRunClick)
                        }
                    )
                    if (!state.isRunActive && state.hasStartedRunning) {
                        FilledTonalIconButton(
                            onClick = {
                                onAction(TrackerAction.OnFinishRunClick)
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Icon(
                                imageVector = FinishIcon,
                                contentDescription = stringResource(id = R.string.finish_run)
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.open_active_run_screen),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ExclamationMarkIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.connect_your_phone),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ToggleRunButton(
    isRunActive: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isRunActive) {
            Icon(
                imageVector = PauseIcon,
                contentDescription = stringResource(R.string.pause_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(R.string.start_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun TrackerScreenPreview() {
    SuperRunnerTheme {
        TrackerScreen(
            state = TrackerState(
                isTrackable = true,
                isRunActive = false,
                hasStartedRunning = true,
                isConnectedPhoneNearby = true,
                heartRate = 80,
                distanceMeters = 5687,
                canTrackHeartRate = true,
                elapsedDuration = 10.minutes + 30.seconds
            )
        )
    }
}