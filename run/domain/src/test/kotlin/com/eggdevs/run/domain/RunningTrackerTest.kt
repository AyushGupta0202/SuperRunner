@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eggdevs.run.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.domain.location.Location
import com.eggdevs.core.domain.location.LocationWithAltitude
import com.eggdevs.core.test.LocationObserverFake
import com.eggdevs.core.test.MainCoroutineExtension
import com.eggdevs.core.test.PhoneToWatchConnectorFake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.math.roundToInt

class RunningTrackerTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var runningTracker: RunningTracker
    private lateinit var locationObserverFake: LocationObserverFake
    private lateinit var phoneToWatchConnectorFake: PhoneToWatchConnectorFake

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: CoroutineScope

    @BeforeEach
    fun setup() {
        locationObserverFake = LocationObserverFake()
        phoneToWatchConnectorFake = PhoneToWatchConnectorFake()

        testDispatcher = mainCoroutineExtension.testDispatcher
        testScope = CoroutineScope(testDispatcher)

        runningTracker = RunningTracker(
            locationObserver = locationObserverFake,
            phoneToWatchConnector = phoneToWatchConnectorFake,
            applicationScope = testScope
        )
    }

    @Test
    fun testCombiningRunData() = runTest {
        runningTracker.runData.test {
            skipItems(1)

            runningTracker.startObservingLocation()
            runningTracker.startTracking()

            val location1 = LocationWithAltitude(Location(1.0, 1.0), 1.0)
            locationObserverFake.trackLocation(location1)
            val emission1 = awaitItem()

            val location2 = LocationWithAltitude(Location(2.0, 2.0), 2.0)
            locationObserverFake.trackLocation(location2)
            val emission2 = awaitItem()

            phoneToWatchConnectorFake.sendFromWatchToPhone(MessagingAction.HeartRateUpdate(160))
            val emission3 = awaitItem()

            phoneToWatchConnectorFake.sendFromWatchToPhone(MessagingAction.HeartRateUpdate(170))
            val emission4 = awaitItem()

            testScope.cancel()

            assertThat(emission1.locations[0][0].locationWithAltitude).isEqualTo(location1)
            assertThat(emission2.locations[0][1].locationWithAltitude).isEqualTo(location2)
            assertThat(emission3.heartRates).isEqualTo(listOf(160))
            assertThat(emission4.heartRates).isEqualTo(listOf(160, 170))

            val expectedDistance = 157.225 * 1000L
            val tolerance = 0.03
            val lowerBound = (expectedDistance * (1 - tolerance)).roundToInt()
            val upperBound = (expectedDistance * (1 + tolerance)).roundToInt()
            assertThat(emission4.distanceMeters).isBetween(lowerBound, upperBound)

            assertThat(emission4.locations.first()).hasSize(2)
        }
    }
}