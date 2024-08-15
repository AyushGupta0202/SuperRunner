package com.eggdevs.core.data.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenRequest(
    val refreshToken: String,
    val userId: String
)
