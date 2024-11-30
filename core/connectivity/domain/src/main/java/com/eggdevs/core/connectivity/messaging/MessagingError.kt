package com.eggdevs.core.connectivity.messaging

import com.eggdevs.core.domain.util.Error

enum class MessagingError: Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}