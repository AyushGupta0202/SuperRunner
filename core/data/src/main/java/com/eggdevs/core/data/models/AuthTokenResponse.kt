package com.eggdevs.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenResponse(
    val accessToken: String?,
    val expirationTimestamp: String?
)
