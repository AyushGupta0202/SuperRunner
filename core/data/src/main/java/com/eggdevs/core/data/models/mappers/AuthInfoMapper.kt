package com.eggdevs.core.data.models.mappers

import com.eggdevs.core.data.models.AuthInfoSerializable
import com.eggdevs.core.domain.models.AuthInfo

fun AuthInfo.toAuthInfoSerializable() = AuthInfoSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)

fun AuthInfoSerializable.toAuthInfo() = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)