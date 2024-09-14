package com.eggdevs.core.data.run

import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.datasource.local.LocalRunDataSource
import com.eggdevs.core.domain.run.datasource.local.RunId
import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope // need a different scope than these methods will run in (probably viewmodelscope) as we don't want to cancel the operations when the viewmodel is cleared
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        // Local DB is our Single Source of Truth
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        // First insert the Run in the Local DB
        return when (val localResult = localRunDataSource.upsertRun(run)) {
            is Result.Error -> localResult.asEmptyDataResult()
            is Result.Success -> {
                // Sync it with the backend after insertion in the Local DB is success
                val runWithId = run.copy(id = localResult.data)
                when (val remoteResult = remoteRunDataSource.postRun(run = runWithId, mapPicture = mapPicture)) {
                    is Result.Error -> Result.Success(Unit) //TODO: to handle later
                    is Result.Success -> {
                        applicationScope.async {
                            localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                        }.await()
                    }
                }
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()
    }
}