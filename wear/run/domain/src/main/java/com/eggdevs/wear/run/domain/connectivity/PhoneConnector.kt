package com.eggdevs.wear.run.domain.connectivity

import com.eggdevs.core.connectivity.domain.DeviceNode
import kotlinx.coroutines.flow.StateFlow

interface PhoneConnector {
    val connectedDevice: StateFlow<DeviceNode?>
}