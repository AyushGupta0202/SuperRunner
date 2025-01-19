package com.eggdevs.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.domain.location.Location
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.presentation.ui.asUiText
import com.eggdevs.run.domain.LocationDataCalculator
import com.eggdevs.run.domain.RunningTracker
import com.eggdevs.run.domain.connectivity.WatchConnector
import com.eggdevs.core.notification.service.ActiveRunService
import com.eggdevs.core.presentation.ui.UiText
import com.eggdevs.run.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val runRepository: RunRepository,
    private val watchConnector: WatchConnector,
    private val applicationScope: CoroutineScope
): ViewModel() {

    var state by mutableStateOf(
        value = ActiveRunState(
            shouldTrack = ActiveRunService.isServiceActive.value && runningTracker.isTracking.value,
            hasStartedRunning = ActiveRunService.isServiceActive.value
        )
    )
        private set

    private val activeRunEventChannel = Channel<ActiveRunEvent>()
    val activeRunEvents = activeRunEventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow {
        state.shouldTrack
    }.stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack,
        hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        watchConnector
            .connectedDevice
            .filterNotNull()
            .onEach { connectedDevice ->
                Timber.d("Connected device: $connectedDevice")
            }
            .launchIn(viewModelScope)

        hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }
            .launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                if (isTracking) {
                    runningTracker.startTracking()
                } else {
                    runningTracker.stopTracking()
                }
            }
            .launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach { locationWithAltitude ->
                state = state.copy(
                    currentLocation = locationWithAltitude?.location
                )
            }
            .launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach { runData ->
                state = state.copy(runData = runData)
            }
            .launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach { elapsedTime ->
                state = state.copy(elapsedTime = elapsedTime)
            }
            .launchIn(viewModelScope)

        listenToWatchActions()
    }

    fun onAction(action: ActiveRunAction, triggeredOnWatch: Boolean = false) {
        if (!triggeredOnWatch) {
            val messagingAction = when(action) {
                ActiveRunAction.OnFinishRunClick -> if (!isLocationsListEmpty()){
                    MessagingAction.Finish
                } else null
                ActiveRunAction.OnResumeRunClick -> MessagingAction.StartOrResume
                ActiveRunAction.OnToggleRunClick -> {
                    if (state.hasStartedRunning) {
                        MessagingAction.Pause
                    } else {
                        MessagingAction.StartOrResume
                    }
                }
                else -> null
            }
            messagingAction?.let {
                viewModelScope.launch {
                    watchConnector.sendActionToWatch(it)
                }
            }
        }

        when(action) {
            ActiveRunAction.OnDismissPermissionRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }
            ActiveRunAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }
            ActiveRunAction.OnFinishRunClick -> {
                if (!isLocationsListEmpty()) {
                    state = state.copy(
//                    shouldTrack = false,
                        isRunFinished = true,
                        isSavingRun = true
                    )
                } else {
                    viewModelScope.launch {
                        activeRunEventChannel.send(ActiveRunEvent.Error(UiText.StringResource(R.string.no_location_data)))
                    }
                }
            }
            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }
            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }
            is ActiveRunAction.SubmitLocationPermissionRationale -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationRationale
                )
            }
            is ActiveRunAction.SubmitNotificationPermissionRationale -> {
                state = state.copy(
                    showNotificationRationale = action.showNotificationRationale
                )
            }
            is ActiveRunAction.OnRunProcessed -> {
                finishRun(action.mapPictureBytes)
            }
            else -> Unit
        }
    }

    private fun finishRun(mapPictureBytes: ByteArray) {
        val locations = state.runData.locations
        // TODO: we can apply this check in tracker map itself
        if (isLocationsListEmpty()) {
            state = state.copy(
                isSavingRun = false
            )
            return
        }
        viewModelScope.launch {
            val run = Run(
                id = null,
                distanceMeters = state.runData.distanceMeters,
                duration = state.elapsedTime,
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(locations),
                mapPictureUrl = null,
                location = state.currentLocation ?: Location(0.0, 0.0),
                dateTimeUtc = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(locations),
                avgHeartRate = if (state.runData.heartRates.isEmpty()) {
                    null
                } else {
                    state.runData.heartRates.average().roundToInt()
                },
                maxHeartRate = if (state.runData.heartRates.isEmpty()) {
                    null
                } else {
                    state.runData.heartRates.max()
                }
            )
            runningTracker.finishRun()

            // save run and send bytes to repository
            when (val result = runRepository.upsertRun(run, mapPictureBytes)) {
                is Result.Error -> {
                    activeRunEventChannel.send(ActiveRunEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    activeRunEventChannel.send(ActiveRunEvent.RunSaved)
                }
            }

            state = state.copy(
                isSavingRun = false
            )
        }
    }

    private fun listenToWatchActions() {
        watchConnector
            .messagingActions
            .onEach { action ->
                when(action) {
                    MessagingAction.ConnectionRequest -> {
                        if (isTracking.value) {
                            watchConnector.sendActionToWatch(MessagingAction.StartOrResume)
                        }
                    }
                    MessagingAction.Finish -> {
                        onAction(
                            action = ActiveRunAction.OnFinishRunClick,
                            triggeredOnWatch = true
                        )
                    }
                    MessagingAction.Pause -> {
                        if (isTracking.value) {
                            onAction(
                                action = ActiveRunAction.OnToggleRunClick,
                                triggeredOnWatch = true
                            )
                        }
                    }
                    MessagingAction.StartOrResume -> {
                        if (!isTracking.value) {
                            if (state.hasStartedRunning) {
                                onAction(
                                    action = ActiveRunAction.OnResumeRunClick,
                                    triggeredOnWatch = true
                                )
                            } else {
                                onAction(
                                    action = ActiveRunAction.OnToggleRunClick,
                                    triggeredOnWatch = true
                                )
                            }
                        }
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isServiceActive.value) {
            applicationScope.launch {
                watchConnector.sendActionToWatch(MessagingAction.Untrackable)
            }
            runningTracker.stopObservingLocation()
        }
    }

    private fun isLocationsListEmpty(): Boolean {
        return state.runData.locations.flatten().isEmpty()
    }
}