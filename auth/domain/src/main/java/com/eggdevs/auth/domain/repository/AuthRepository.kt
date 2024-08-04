package com.eggdevs.auth.domain.repository

import com.eggdevs.auth.domain.models.LoginResponse
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun login(email: String, password: String): Result<LoginResponse, DataError.Network>
}