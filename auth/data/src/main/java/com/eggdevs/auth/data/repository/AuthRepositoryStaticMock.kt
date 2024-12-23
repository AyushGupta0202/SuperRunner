package com.eggdevs.auth.data.repository

import com.eggdevs.auth.domain.repository.AuthRepository
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.models.AuthInfo
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult

class AuthRepositoryStaticMock(
    private val sessionStorage: SessionStorage
): AuthRepository {
    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        sessionStorage.setInfo(
            AuthInfo(
                accessToken = "1234",
                refreshToken = "1234",
                userId = "1234"
            )
        )
        return com.eggdevs.core.domain.util.Result.Success(Unit)
    }

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return com.eggdevs.core.domain.util.Result.Success(Unit)
    }
}