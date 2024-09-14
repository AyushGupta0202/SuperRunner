package com.eggdevs.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eggdevs.core.database.entities.DeletedRunPendingSyncEntity

@Dao
interface DeletedRunPendingSyncDao {
    @Query("SELECT * FROM DeletedRunPendingSyncEntity WHERE userId=:userId")
    suspend fun getAllDeletedRunPendingSyncEntities(userId: String): List<DeletedRunPendingSyncEntity>
    @Upsert
    suspend fun upsertDeleteRunPendingSyncEntity(deletedRunPendingSyncEntity: DeletedRunPendingSyncEntity)
    @Query("DELETE FROM DeletedRunPendingSyncEntity WHERE runId=:runId")
    suspend fun deleteDeletedRunPendingSyncEntity(runId: String)
}