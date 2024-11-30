package com.eggdevs.core.connectivity.messaging

import com.eggdevs.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface MessagingClient {
    fun connectToNode(nodeId: String): Flow<MessagingAction>
    suspend fun sendOrQueueAction(messagingAction: MessagingAction): EmptyResult<MessagingError>
}