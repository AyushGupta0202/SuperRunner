@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eggdevs.run.domain

import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.domain.Timer
import com.eggdevs.core.domain.location.LocationWithAltitudeTimestamp
import com.eggdevs.run.domain.connectivity.WatchConnector
import com.eggdevs.run.domain.models.RunData
import com.eggdevs.run.domain.models.replaceLast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val phoneToWatchConnector: WatchConnector,
    applicationScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(RunData())
    val runData get() = _runData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking get() = _isTracking.asStateFlow()

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime get() = _elapsedTime.asStateFlow()

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

    private val heartRates = isTracking
        .flatMapLatest { isTracking ->
            if (isTracking) {
                phoneToWatchConnector.messagingActions
            } else flowOf()
        }
        .filterIsInstance<MessagingAction.HeartRateUpdate>()
        .map { it.heartRate }
        .runningFold(initial = emptyList<Int>()) { currentHeartRates, newHeartRate ->
            currentHeartRates + newHeartRate
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            emptyList()
        )

    init {
        _isTracking
            .onEach { isTracking ->
                if (!isTracking) {
                    val newLocationsList = buildList {
                        addAll(runData.value.locations)
                        add(emptyList<LocationWithAltitudeTimestamp>())
                    }.toList()
                    _runData.update {
                        it.copy(
                            locations = newLocationsList
                        )
                    }
                }
            }
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
            .combineTransform(_isTracking) { location, isTracking ->
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
            .combine(heartRates) { location, heartRates ->
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
                        pace = avgSecondsPerKm.seconds,
                        heartRates = heartRates
                    )
                }
            }
            .launchIn(applicationScope)

        elapsedTime
            .onEach {
                phoneToWatchConnector.sendActionToWatch(MessagingAction.TimeUpdate(it))
            }
            .launchIn(applicationScope)

        runData
            .map { it.distanceMeters }
            .distinctUntilChanged()
            .onEach {
                phoneToWatchConnector.sendActionToWatch(MessagingAction.DistanceUpdate(it))
            }
            .launchIn(applicationScope)
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
        phoneToWatchConnector.setIsTrackable(true)
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
        phoneToWatchConnector.setIsTrackable(false)
    }

    fun startTracking() {
        _isTracking.value = true
    }

    fun stopTracking() {
        _isTracking.value = false
    }

    /** This is a shared singleton state. We have to make sure
    * to clear the state when the run is finished.
    */
    fun finishRun() {
        stopObservingLocation()
        stopTracking()
        _elapsedTime.value = Duration.ZERO
        _runData.value = RunData()
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