package com.eggdevs.core.domain.run.datasource.local

import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias RunId = String

interface LocalRunDataSource {
    fun getRuns(): Flow<List<Run>>
    suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local>
    suspend fun upsertRun(run: Run): Result<RunId, DataError.Local>
    suspend fun deleteRun(id: RunId)
    suspend fun deleteAllRuns()
}