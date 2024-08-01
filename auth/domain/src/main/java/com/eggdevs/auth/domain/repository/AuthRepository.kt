package com.eggdevs.auth.domain.repository

import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}