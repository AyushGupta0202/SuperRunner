package com.eggdevs.auth.data.repository

import com.eggdevs.auth.data.models.LoginRequest
import com.eggdevs.auth.data.models.LoginResponse
import com.eggdevs.auth.data.models.RegisterRequest
import com.eggdevs.auth.domain.repository.AuthRepository
import com.eggdevs.core.data.networking.post
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.models.AuthInfo
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val response = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (response is Result.Success) {
            sessionStorage.setInfo(
                AuthInfo(
                    accessToken = response.data.accessToken ?: "",
                    refreshToken = response.data.refreshToken ?: "",
                    userId = response.data.userId ?: ""
                )
            )
        }
        return response.asEmptyDataResult()
    }
}