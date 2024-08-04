package com.eggdevs.auth.data.repository

import com.eggdevs.auth.data.models.LoginRequestSerializable
import com.eggdevs.auth.data.models.LoginResponseSerializable
import com.eggdevs.auth.data.models.RegisterRequest
import com.eggdevs.auth.data.models.mappers.toLoginResponse
import com.eggdevs.auth.domain.models.LoginResponse
import com.eggdevs.auth.domain.repository.AuthRepository
import com.eggdevs.core.data.networking.post
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
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

    override suspend fun login(email: String, password: String): Result<LoginResponse, DataError.Network> {
        val response = httpClient.post<LoginRequestSerializable, LoginResponseSerializable>(
            route = "/login",
            body = LoginRequestSerializable(
                email = email,
                password = password
            )
        )
        return when(response) {
            is Result.Error -> {
                response
            }
            is Result.Success -> {
                Result.Success(
                    response.data.toLoginResponse()
                )
            }
        }
    }
}