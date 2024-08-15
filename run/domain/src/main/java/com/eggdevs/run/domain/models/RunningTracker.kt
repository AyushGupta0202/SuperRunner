@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eggdevs.run.domain.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
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

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }
}

fun main() {
    val i = 11
    val g = if (i < 10) {
        flowOf(i)
    } else {
        flowOf()
    }
    runBlocking {
        g.collect {
            println("flow collect")
            println(it)
        }
    }
}