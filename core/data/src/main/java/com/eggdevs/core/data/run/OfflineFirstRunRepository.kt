package com.eggdevs.core.data.run

import com.eggdevs.core.data.networking.get
import com.eggdevs.core.database.dao.DeletedRunPendingSyncDao
import com.eggdevs.core.database.dao.RunPendingSyncDao
import com.eggdevs.core.database.mappers.toRun
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.run.Run
import com.eggdevs.core.domain.run.SyncRunScheduler
import com.eggdevs.core.domain.run.datasource.local.LocalRunDataSource
import com.eggdevs.core.domain.run.datasource.local.RunId
import com.eggdevs.core.domain.run.datasource.remote.RemoteRunDataSource
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.core.domain.util.DataError
import com.eggdevs.core.domain.util.EmptyResult
import com.eggdevs.core.domain.util.Result
import com.eggdevs.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val deletedRunPendingSyncDao: DeletedRunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler,
    private val httpClient: HttpClient,
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
                    is Result.Error -> {
                        applicationScope.launch {
                            syncRunScheduler.scheduleSync(
                                    type = SyncRunScheduler.SyncType.CreateRun(
                                        run = runWithId,
                                        mapPictureBytes = mapPicture
                                    )
                                )
                        }.join()
                        Result.Success(Unit)
                    }
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

        // Edge case where the run is created in offline-mode,
        // and then deleted in the offline-mode as well. In that case,
        // we don't need to sync anything.
        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                        type = SyncRunScheduler.SyncType.DeleteRun(id)
                    )
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.getInfo()?.userId ?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }
            val deletedRuns = async {
                deletedRunPendingSyncDao.getAllDeletedRunPendingSyncEntities(userId)
            }

            val createdRunJob = createdRuns
                .await()
                .map { runPendingSyncEntity ->
                    launch {
                        val run = runPendingSyncEntity.run.toRun()
                        when(remoteRunDataSource.postRun(run, runPendingSyncEntity.mapPictureBytes)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(runPendingSyncEntity.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deletedRunJob = deletedRuns
                .await()
                .map { deletedRunPendingSyncEntity ->
                    launch {
                        when(remoteRunDataSource.deleteRun(deletedRunPendingSyncEntity.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    deletedRunPendingSyncDao.deleteDeletedRunPendingSyncEntity(deletedRunPendingSyncEntity.runId)
                                }.join()
                            }
                        }
                    }
                }
            createdRunJob.joinAll()
            deletedRunJob.joinAll()
        }
    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val result = httpClient.get<Unit>(
            route = "/logout"
        ).asEmptyDataResult()

        httpClient.authProviders.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result
    }
}

fun main() = runBlocking{
//    val list = listOf(3, 2, 1)
//    val timeInMillisLaunch = measureTimeMillis {
//        val jobs = list.map {
//            launch {
//                delay(it * 1000L)
//                launch {
//                    delay(it * 1000L)
//                    println(it)
//                }.join()
//            }
//        }
//        jobs.joinAll()
//    }
//    println("launchtime: $timeInMillisLaunch")
//
//    val timeInMillisAsync = measureTimeMillis {
//        val jobs = list.map {
//            async {
//                delay(it * 1000L)
//                async {
//                    delay(it * 1000L)
//                    println(it)
//                }.await()
//            }
//        }
//        jobs.awaitAll()
//    }
//    println("launchtime: $timeInMillisAsync")

    val async1 = async {
//        delay(1000L)
        println("async 1")
        "value1"
    }
    val async2 = async {
//        delay(1000L)
        println("async 2")
        "value2"
    }
    delay(2000L)
    println("async1: ${async1.await()}")
    println("async2: ${async2.await()}")
}