package com.eggdevs.core.data.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenResponse(
    val accessToken: String?,
    val expirationTimestamp: String?
)
