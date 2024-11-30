package com.eggdevs.run.domain.connectivity

import com.eggdevs.core.connectivity.domain.DeviceNode
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.connectivity.messaging.MessagingError
import com.eggdevs.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevice: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError>

    fun setIsTrackable(isTrackable: Boolean)
}