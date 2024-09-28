package com.eggdevs.core.data.networking

import com.eggdevs.core.data.BuildConfig
import com.eggdevs.core.data.auth.models.AuthTokenRequest
import com.eggdevs.core.data.auth.models.AuthTokenResponse
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

class HttpClientFactory(
    private val sessionStorage: SessionStorage
) {
    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("x-api-key", BuildConfig.API_KEY)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val authInfo = sessionStorage.getInfo()
                        BearerTokens(
                            accessToken = authInfo?.accessToken ?: "",
                            refreshToken = authInfo?.refreshToken ?: ""
                        )
                    }
                    refreshTokens {
                        val authInfo = sessionStorage.getInfo()
                        val tokenRefreshResponse = client.post<AuthTokenRequest, AuthTokenResponse>(
                            route = "/accessToken",
                            body = AuthTokenRequest(
                                refreshToken = authInfo?.refreshToken ?: "",
                                userId = authInfo?.userId ?: ""
                            )
                        )

                        when (tokenRefreshResponse) {
                            is Result.Error -> {
                                BearerTokens(
                                    accessToken = "",
                                    refreshToken = ""
                                )
                            }
                            is Result.Success -> {
                                val newAuthInfo = authInfo?.copy(
                                    accessToken = tokenRefreshResponse.data.accessToken ?: ""
                                )
                                sessionStorage.setInfo(newAuthInfo)
                                BearerTokens(
                                    accessToken = newAuthInfo?.accessToken ?: "",
                                    refreshToken = newAuthInfo?.refreshToken ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}