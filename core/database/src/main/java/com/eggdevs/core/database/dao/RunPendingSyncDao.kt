package com.eggdevs.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eggdevs.core.database.entities.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {
    @Query("SELECT * FROM RunPendingSyncEntity WHERE userId=:userId")
    suspend fun getAllRunPendingSyncEntities(userId: String): List<RunPendingSyncEntity>
    @Query("SELECT * FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun getRunPendingSyncEntity(runId: String): RunPendingSyncEntity?
    @Upsert
    suspend fun upsertRunPendingSyncEntity(runPendingSyncEntity: RunPendingSyncEntity)
    @Query("DELETE FROM RunPendingSyncEntity WHERE runId=:runId")
    suspend fun deleteRunPendingSyncEntity(runId: String)
}