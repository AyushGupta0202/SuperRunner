package com.eggdevs.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.run.domain.models.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
): ViewModel() {

    var state by mutableStateOf(ActiveRunState())
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
    }

    fun onAction(action: ActiveRunAction) {
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
                state = state.copy(
                    shouldTrack = false,
                    isRunFinished = true
                )
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
            else -> Unit
        }
    }
}