package com.eggdevs.auth.domain.models

data class LoginResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenExpirationTimestamp: Long?,
    val userId: String?
)
