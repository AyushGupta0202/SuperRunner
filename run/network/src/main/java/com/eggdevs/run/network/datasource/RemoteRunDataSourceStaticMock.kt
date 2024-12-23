package com.eggdevs.run.network.datasource

import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.run.network.datasource.mappers.toRun
import com.eggdevs.run.network.datasource.models.RunDto

class RemoteRunDataSourceStaticMock: RemoteRunDataSource {

    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        val runDtos = List(10) { index ->
            RunDto(
                id = "id_$index",
                dateTimeUtc = "2023-10-1${index}T00:00:00Z",
                durationMillis = 3600000L + 60000 * index,
                distanceMeters = 10000 + 100 * index,
                lat = 37.7749 + index,
                long = -122.4194 + index,
                avgSpeedKmh = 10.0 + index,
                maxSpeedKmh = 15.0 + index,
                totalElevationMeters = 10 + index,
                mapPictureUrl = null,
                avgHeartRate = 120 + index,
                maxHeartRate = 150 + index
            )
        }
        return Result.Success(runDtos.map { it.toRun() })
    }

    override suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network> {
        return Result.Success(run)
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.Network> {
        return Result.Success(Unit)
    }
}