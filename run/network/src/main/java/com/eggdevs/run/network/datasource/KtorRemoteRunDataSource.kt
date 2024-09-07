package com.eggdevs.run.network.datasource

import com.eggdevs.core.data.networking.constructRoute
import com.eggdevs.core.data.networking.delete
import com.eggdevs.core.data.networking.get
import com.eggdevs.core.data.networking.safeCall
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.domain.util.map
import com.eggdevs.run.network.datasource.mappers.toCreateRunRequest
import com.eggdevs.run.network.datasource.mappers.toRun
import com.eggdevs.run.network.datasource.mappers.toRuns
import com.eggdevs.run.network.datasource.models.RunDto
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRemoteRunDataSource(
    private val httpClient: HttpClient
): RemoteRunDataSource {
    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return httpClient.get<List<RunDto>>(
            route = "/runs"
        ).map { runDtos ->
            runDtos.toRuns()
        }
    }

    override suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network> {
        val createRunRequestJson = Json.encodeToString(run.toCreateRunRequest())
        val result = safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(
                url = constructRoute("/runs"),
                formData = formData {
                    append(
                        key = "MAP_PICTURE",
                        value = mapPicture,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=mappicture.jpg")
                        }
                    )
                    append(
                        key = "RUN_DATA",
                        value = createRunRequestJson,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "text/plain")
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"RUN_DATA\"")
                        }
                    )
                }
            ) {
                method = HttpMethod.Post
            }
        }

        return result.map { runDto ->
            runDto.toRun()
        }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = "/run",
            queryParameters = mapOf(
                "id" to id
            )
        )
    }
}