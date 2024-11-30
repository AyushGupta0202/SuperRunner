package com.eggdevs.core.connectivity.data.messaging

import android.content.Context
import com.eggdevs.core.connectivity.data.mappers.toMessagingAction
import com.eggdevs.core.connectivity.data.mappers.toMessagingActionDto
import com.eggdevs.core.connectivity.messaging.MessagingAction
import com.eggdevs.core.connectivity.messaging.MessagingClient
import com.eggdevs.core.connectivity.messaging.MessagingError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WearMessagingClient(
    context: Context
): MessagingClient {

    private val wearMessageClient: MessageClient = Wearable.getMessageClient(context)

    private val messageQueue = mutableListOf<MessagingAction>()
    private var connectedNodeId: String? = null

    override fun connectToNode(nodeId: String): Flow<MessagingAction> {
        connectedNodeId = nodeId
        return callbackFlow {
            val listener: (MessageEvent) -> Unit = { event ->
                if (event.path.startsWith(BASE_PATH_MESSAGING_ACTION)) {
                    val jsonString = event.data.decodeToString()
                    val action = Json.decodeFromString<MessagingActionDto>(jsonString)
                    trySend(action.toMessagingAction())
                }
            }

            wearMessageClient.addListener(listener)

            messageQueue.forEach {
                sendOrQueueAction(it)
            }
            messageQueue.clear()

            awaitClose {
                wearMessageClient.removeListener(listener)
            }
        }
    }

    override suspend fun sendOrQueueAction(messagingAction: MessagingAction): EmptyResult<MessagingError> {
        return connectedNodeId?.let { id ->
            try {
                val jsonString = Json.encodeToString(messagingAction.toMessagingActionDto())
                wearMessageClient.sendMessage(id, BASE_PATH_MESSAGING_ACTION, jsonString.encodeToByteArray()).await()
                Result.Success(Unit)
            } catch (e: ApiException) {
                Result.Error(
                    if (e.status.isInterrupted) {
                        MessagingError.CONNECTION_INTERRUPTED
                    } else MessagingError.UNKNOWN
                )
            }
        } ?: run {
            messageQueue.add(messagingAction)
            Result.Error(MessagingError.DISCONNECTED)
        }
    }

    companion object {
        private const val BASE_PATH_MESSAGING_ACTION = "superrunner/messaging_action"
    }
}