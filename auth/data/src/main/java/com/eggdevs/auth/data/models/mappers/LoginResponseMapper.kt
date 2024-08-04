package com.eggdevs.auth.data.models.mappers

import com.eggdevs.auth.data.models.LoginResponseSerializable
import com.eggdevs.auth.domain.models.LoginResponse

fun LoginResponseSerializable.toLoginResponse() = LoginResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenExpirationTimestamp = accessTokenExpirationTimestamp,
    userId = userId
)

fun LoginResponse.toLoginResponseSerializable() = LoginResponseSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    accessTokenExpirationTimestamp = tokenExpirationTimestamp,
    userId = userId
)