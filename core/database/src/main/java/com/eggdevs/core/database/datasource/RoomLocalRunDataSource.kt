package com.eggdevs.core.database.datasource

import android.database.sqlite.SQLiteFullException
import com.eggdevs.core.database.dao.RunDao
import com.eggdevs.core.database.mappers.toRunEntities
import com.eggdevs.core.database.mappers.toRunEntity
import com.eggdevs.core.database.mappers.toRuns
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.datasource.local.LocalRunDataSource
import com.eggdevs.core.domain.run.datasource.local.RunId
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalRunDataSource(
    private val runDao: RunDao
): LocalRunDataSource {

    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns().map {
            it.toRuns()
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return try {
            val runEntities = runs.toRunEntities()
            runDao.upsertRuns(runEntities)
            Result.Success(runEntities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val runEntity = run.toRunEntity()
            runDao.upsertRun(runEntity)
            Result.Success(runEntity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteRun(id: RunId) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}