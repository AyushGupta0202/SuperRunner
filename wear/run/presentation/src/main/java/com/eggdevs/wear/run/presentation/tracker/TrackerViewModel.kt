package com.eggdevs.wear.run.presentation.tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.wear.run.domain.connectivity.PhoneConnector
import com.eggdevs.wear.run.domain.tracker.ExerciseTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TrackerViewModel(
    private val exerciseTracker: ExerciseTracker,
    phoneConnector: PhoneConnector
): ViewModel() {

    var state by mutableStateOf(TrackerState())
        private set

    private val hasBodySensorsPermission = MutableStateFlow(false)

    init {
        phoneConnector
            .connectedDevice
            .filterNotNull()
            .onEach { connectedDevice ->
                state = state.copy(
                    isConnectedPhoneNearby = connectedDevice.isNearby
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: TrackerAction) {
        when(action) {
            TrackerAction.OnFinishRunClick -> Unit
            TrackerAction.OnToggleRunClick -> Unit
            is TrackerAction.OnBodySensorsPermissionResult -> {
                hasBodySensorsPermission.value = action.acceptedBodySensorsPermission
                if (action.acceptedBodySensorsPermission) {
                    viewModelScope.launch {
                        val isHeartRateTrackingSupported = exerciseTracker.isHeartRateTrackingSupported()
                        state = state.copy(
                            canTrackHeartRate = isHeartRateTrackingSupported
                        )
                    }
                }
            }
        }
    }
}