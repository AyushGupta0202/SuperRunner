@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eggdevs.run.domain.models

import com.eggdevs.core.domain.Timer
import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(RunData())
    val runData get() = _runData

    private val isTracking = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)

    private val isObservingLocation = MutableStateFlow(false)

    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observeLocation(1000)
            } else {
                emptyFlow()
            }
        }.stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    init {
        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    Timer.timeAndEmit()
                } else {
                    emptyFlow()
                }
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        currentLocation
            .filterNotNull()
            .combineTransform(isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationWithAltitudeTimestamp(
                    locationWithAltitude = location,
                    durationTimestamp = elapsedTime
                )
            }
            .onEach { location ->
                val currentLocationsList = runData.value.locations
                val lastLocationsList = if (currentLocationsList.isNotEmpty()) {
                    currentLocationsList.last() + location
                } else {
                    listOf(location)
                }
                val newLocationsList = currentLocationsList.replaceLast(lastLocationsList)
                val distanceMeters = LocationDataCalculator.getTotalDistanceInMeters(
                    locations = newLocationsList
                )
                val distanceKm = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp

                val avgSecondsPerKm = if (distanceKm == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }

                _runData.update {
                    RunData(
                        locations = newLocationsList,
                        distanceMeters = distanceMeters,
                        pace = avgSecondsPerKm.seconds
                    )
                }
            }
            .launchIn(applicationScope)
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun startTracking() {
        isTracking.value = true
    }

    fun stopTracking() {
        isTracking.value = false
    }
}

fun main() {
    val i = 11
    val g = if (i < 10) {
        flowOf(i)
    } else {
        flowOf()
    }
    val flow1 = flowOf(1, 2, 3)
    val flow2 = flowOf("a", "b", "c", "d", "e")
    val zippedFlow = flow1.zip(flow2) { number, letter -> "$number$letter" }
    runBlocking {
        zippedFlow.collect {
            println("flow collect")
            println(it)
        }
    }
}