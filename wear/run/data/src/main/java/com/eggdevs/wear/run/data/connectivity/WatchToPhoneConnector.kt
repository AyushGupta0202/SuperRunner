@file:OptIn(ExperimentalCoroutinesApi::class)

package com.eggdevs.wear.run.data.connectivity

import com.eggdevs.core.connectivity.domain.DeviceNode
import com.eggdevs.core.connectivity.domain.DeviceType
import com.eggdevs.core.connectivity.domain.NodeDiscovery
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.connectivity.messaging.MessagingClient
import com.eggdevs.core.connectivity.messaging.MessagingError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.wear.run.domain.connectivity.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

class WatchToPhoneConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
): PhoneConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedDevice = _connectedNode.asStateFlow()

    override val messagingActions = nodeDiscovery
        .observeConnectedDevices(DeviceType.WATCH)
        .flatMapLatest { connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if (node != null && node.isNearby) {
                _connectedNode.value = node
                messagingClient.connectToNode(node.id)
            } else flowOf()
        }
        .shareIn(
            applicationScope,
            SharingStarted.Eagerly
        )

    override suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }
}