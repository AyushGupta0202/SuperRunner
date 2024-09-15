package com.eggdevs.run.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eggdevs.core.database.dao.DeletedRunPendingSyncDao
import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.run.data.workers.mappers.toWorkerResult

class DeleteRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val deletedRunPendingSyncDao: DeletedRunPendingSyncDao,
    private val remoteRunDataSource: RemoteRunDataSource
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val pendingRunId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        return when(val result = remoteRunDataSource.deleteRun(pendingRunId)) {
            is com.eggdevs.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.eggdevs.core.domain.util.Result.Success -> {
                deletedRunPendingSyncDao.deleteDeletedRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }
}