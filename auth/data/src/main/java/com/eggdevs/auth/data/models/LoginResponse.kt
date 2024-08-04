package com.eggdevs.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpirationTimestamp: Long?,
    val userId: String?
)
