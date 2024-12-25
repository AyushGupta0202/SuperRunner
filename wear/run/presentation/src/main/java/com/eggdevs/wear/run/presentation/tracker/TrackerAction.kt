package com.eggdevs.wear.run.presentation.tracker

sealed interface TrackerAction {
    data object OnToggleRunClick: TrackerAction
    data object OnFinishRunClick: TrackerAction
    class OnBodySensorsPermissionResult(val acceptedBodySensorsPermission: Boolean): TrackerAction
    data class OnEnterAmbientMode(val burnInProtectionRequired: Boolean): TrackerAction
    data object OnExitAmbientMode: TrackerAction
}