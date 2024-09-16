package com.eggdevs.run.data.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.eggdevs.core.database.dao.DeletedRunPendingSyncDao
import com.eggdevs.core.database.dao.RunPendingSyncDao
import com.eggdevs.core.database.entities.DeletedRunPendingSyncEntity
import com.eggdevs.core.database.entities.RunPendingSyncEntity
import com.eggdevs.core.database.mappers.toRunEntity
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val workManager: WorkManager = WorkManager.getInstance(context),
    private val sessionStorage: SessionStorage,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val deletedRunPendingSyncDao: DeletedRunPendingSyncDao,
    private val applicationScope: CoroutineScope
): SyncRunScheduler {
    override suspend fun scheduleSync(type: SyncRunScheduler.SyncType) {
        when(type) {
            is SyncRunScheduler.SyncType.CreateRun -> {
                scheduleCreateRunWorker(type.run, type.mapPictureBytes)
            }
            is SyncRunScheduler.SyncType.DeleteRun -> {
                scheduleDeleteRunWorker(type.runId)
            }
            is SyncRunScheduler.SyncType.FetchRuns -> {
                scheduleFetchRunWorker(type.interval)
            }
        }
    }

    private suspend fun scheduleCreateRunWorker(run: Run, mapPictureBytes: ByteArray) {
        val userId = sessionStorage.getInfo()?.userId ?: return
        val runPendingSyncEntity = RunPendingSyncEntity(
            run = run.toRunEntity(),
            mapPictureBytes = mapPictureBytes,
            userId = userId
        )

        runPendingSyncDao.upsertRunPendingSyncEntity(runPendingSyncEntity)

        val createRunWorker = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(RUN_ID, run.id)
                    .build()
            )
            .addTag("create_work")
            .build()

        applicationScope.launch {
            workManager
                .enqueue(createRunWorker)
                .await()
        }.join()
    }

    private suspend fun scheduleDeleteRunWorker(runId: String) {
        val userId = sessionStorage.getInfo()?.userId ?: return
        val deletedRunPendingSyncEntity = DeletedRunPendingSyncEntity(
            runId = runId,
            userId = userId
        )

        deletedRunPendingSyncDao.upsertDeleteRunPendingSyncEntity(deletedRunPendingSyncEntity)

        val deleteRunWorker = OneTimeWorkRequestBuilder<DeleteRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(RUN_ID, runId)
                    .build()
            )
            .addTag("delete_work")
            .build()

        applicationScope.launch {
            workManager
                .enqueue(deleteRunWorker)
                .await()
        }.join()
    }

    private suspend fun scheduleFetchRunWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val periodicWorkRequest = PeriodicWorkRequestBuilder<FetchRunsWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager
            .enqueue(periodicWorkRequest)
            .await()
    }

    override suspend fun cancelAllSyncs() {
        workManager
            .cancelAllWork()
            .await()
    }
}