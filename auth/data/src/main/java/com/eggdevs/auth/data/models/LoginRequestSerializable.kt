package com.eggdevs.auth.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestSerializable(
    val email: String,
    val password: String
)
