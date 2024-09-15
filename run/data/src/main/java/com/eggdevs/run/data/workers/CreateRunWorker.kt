package com.eggdevs.run.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eggdevs.core.database.dao.RunPendingSyncDao
import com.eggdevs.core.database.mappers.toRun
import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.core.domain.util.Result
import com.eggdevs.run.data.workers.mappers.toWorkerResult

const val RUN_ID = "RUN_ID"

class CreateRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val remoteRunDataSource: RemoteRunDataSource
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        val pendingRunEntity = runPendingSyncDao.getRunPendingSyncEntity(pendingRunId) ?: return Result.failure()
        val run = pendingRunEntity.run.toRun()
        return when(val result = remoteRunDataSource.postRun(run, pendingRunEntity.mapPictureBytes)) {
            is com.eggdevs.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.eggdevs.core.domain.util.Result.Success -> {
                runPendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }
}