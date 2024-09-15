package com.eggdevs.run.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.run.data.workers.mappers.toWorkerResult

class FetchRunsWorker(
    context: Context,
    params: WorkerParameters,
    private val runRepository: RunRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        return when(val result = runRepository.fetchRuns()) {
            is com.eggdevs.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.eggdevs.core.domain.util.Result.Success -> {
                Result.success()
            }
        }
    }
}