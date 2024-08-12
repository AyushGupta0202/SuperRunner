package com.eggdevs.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClick: ActiveRunAction
    data object OnFinishRunClick: ActiveRunAction
    data object OnResumeRunClick: ActiveRunAction
    data object OnBackClick: ActiveRunAction
    data class SubmitLocationPermissionRationale(
        val acceptedLocationPermission: Boolean = false,
        val showLocationRationale: Boolean = false
    ): ActiveRunAction
    data class SubmitNotificationPermissionRationale(
        val acceptedNotificationPermission: Boolean = false,
        val showNotificationRationale: Boolean = false
    ): ActiveRunAction
    data object OnDismissPermissionRationaleDialog: ActiveRunAction
}