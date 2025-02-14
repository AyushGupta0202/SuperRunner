package com.eggdevs.auth.presentation.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.eggdevs.auth.data.EmailValidator
import com.eggdevs.auth.data.models.LoginRequest
import com.eggdevs.auth.data.repository.AuthRepositoryImpl
import com.eggdevs.auth.domain.UserDataValidator
import com.eggdevs.core.android_test.SessionStorageFake
import com.eggdevs.core.android_test.TestMockEngine
import com.eggdevs.core.android_test.loginResponseStub
import com.eggdevs.core.data.networking.HttpClientFactory
import com.eggdevs.core.test.MainCoroutineExtension
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class LoginViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var repositoryImpl: AuthRepositoryImpl
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var mockEngine: TestMockEngine

    @BeforeEach
    fun setUp() {
        sessionStorageFake = SessionStorageFake()

        val mockEngineConfig = MockEngineConfig().apply {
            requestHandlers.add { request ->
                val relativeUrl = request.url.encodedPath
                if (relativeUrl == "/login") {
                    respond(
                        content = ByteReadChannel(
                            text = Json.encodeToString(loginResponseStub)
                        ),
                        headers = headers {
                            set("Content-Type", "application/json")
                        }
                    )
                } else {
                    respond(
                        content = byteArrayOf(),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
        mockEngine = TestMockEngine(
            dispatcher = mainCoroutineExtension.testDispatcher,
            mockEngineConfig = mockEngineConfig
        )

        val httpClient = HttpClientFactory(
            sessionStorage = sessionStorageFake
        ).build(mockEngine)
        repositoryImpl = AuthRepositoryImpl(
            httpClient = httpClient,
            sessionStorage = sessionStorageFake
        )

        loginViewModel = LoginViewModel(
            authRepository = repositoryImpl,
            userDataValidator = UserDataValidator(
                patternValidator = EmailValidator
            )
        )
    }

    @Test
    fun testLogin() = runTest {
        assertThat(loginViewModel.state.canLogin).isFalse()

        loginViewModel.state.email.edit {
            append("test@test.com")
        }

        loginViewModel.state.password.edit {
            append("Test12345")
        }

        loginViewModel.onAction(LoginAction.OnLoginClick)

        assertThat(loginViewModel.state.isLoggingIn).isFalse()
        assertThat(loginViewModel.state.email.text.toString()).isEqualTo("test@test.com")
        assertThat(loginViewModel.state.password.text.toString()).isEqualTo("Test12345")

        val loginRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/login"
        }

        assertThat(loginRequest).isNotNull()
        assertThat(loginRequest!!.headers.contains("x-api-key")).isTrue()

        val loginBody = Json.decodeFromString<LoginRequest>(
            loginRequest.body.toByteArray().decodeToString()
        )

        assertThat(loginBody.email).isEqualTo("test@test.com")
        assertThat(loginBody.password).isEqualTo("Test12345")

        val session = sessionStorageFake.getInfo()

        assertThat(session?.userId).isEqualTo(loginResponseStub.userId)
        assertThat(session?.accessToken).isEqualTo(loginResponseStub.accessToken)
        assertThat(session?.refreshToken).isEqualTo(loginResponseStub.refreshToken)
    }
}