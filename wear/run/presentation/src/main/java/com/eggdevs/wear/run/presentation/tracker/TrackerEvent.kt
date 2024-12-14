package com.eggdevs.wear.run.presentation.tracker

import com.eggdevs.core.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
    data class Error(val message: UiText): TrackerEvent
}