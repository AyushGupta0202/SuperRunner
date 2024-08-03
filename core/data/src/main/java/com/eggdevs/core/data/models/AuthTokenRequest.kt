package com.eggdevs.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenRequest(
    val refreshToken: String,
    val userId: String
)
