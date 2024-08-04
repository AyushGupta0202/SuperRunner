package com.eggdevs.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String?,
    val refreshToken: String?,
    val userId: String?
)
