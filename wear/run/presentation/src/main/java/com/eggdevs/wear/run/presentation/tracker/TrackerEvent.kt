package com.eggdevs.wear.run.presentation.tracker

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
}