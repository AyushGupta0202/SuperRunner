package com.eggdevs.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseSerializable(
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpirationTimestamp: Long?,
    val userId: String?
)
