package com.eggdevs.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.run.domain.models.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
): ViewModel() {

    var state by mutableStateOf(ActiveRunState())
        private set

    private val activeRunEventChannel = Channel<ActiveRunEvent>()
    val activeRunEvents = activeRunEventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)

    init {
        _hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }
            .launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach {  location ->
                Timber.d("new location: $location")
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
            ActiveRunAction.OnFinishRunClick -> {

            }
            ActiveRunAction.OnResumeRunClick -> {

            }
            ActiveRunAction.OnToggleRunClick -> {

            }
            is ActiveRunAction.SubmitLocationPermissionRationale -> {
                _hasLocationPermission.value = action.acceptedLocationPermission
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