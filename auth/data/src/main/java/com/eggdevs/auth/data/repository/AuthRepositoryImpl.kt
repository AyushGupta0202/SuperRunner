package com.eggdevs.auth.data.repository

import com.eggdevs.auth.data.models.RegisterRequest
import com.eggdevs.auth.domain.repository.AuthRepository
import com.eggdevs.core.data.networking.post
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
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
}