package com.eggdevs.core.data.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String?,
    val refreshToken: String?,
    val userId: String?
)
