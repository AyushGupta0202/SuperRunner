package com.eggdevs.core.test

import com.eggdevs.core.connectivity.domain.DeviceNode
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.connectivity.messaging.MessagingError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.run.domain.connectivity.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PhoneToWatchConnectorFake: WatchConnector {

    var sendError: MessagingError? = null

    private val _isTrackable = MutableStateFlow(false)

    private val _connectedDevice = MutableStateFlow<DeviceNode?>(null)

    private val _messagingActions = MutableSharedFlow<MessagingAction>()

    override val connectedDevice: StateFlow<DeviceNode?>
        get() = _connectedDevice.asStateFlow()

    override val messagingActions: Flow<MessagingAction>
        get() = _messagingActions.asSharedFlow()

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return if (sendError == null) {
            Result.Success(Unit)
        } else {
            Result.Error(sendError!!)
        }
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        _isTrackable.value = isTrackable
    }

    suspend fun sendFromWatchToPhone(action: MessagingAction) {
        _messagingActions.emit(action)
    }
}