package com.eggdevs.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eggdevs.core.database.dao.AnalyticsDao
import com.eggdevs.core.database.dao.DeletedRunPendingSyncDao
import com.eggdevs.core.database.dao.RunDao
import com.eggdevs.core.database.dao.RunPendingSyncDao
import com.eggdevs.core.database.entities.DeletedRunPendingSyncEntity
import com.eggdevs.core.database.entities.RunEntity
import com.eggdevs.core.database.entities.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunPendingSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase: RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val deletedRunPendingSyncDao: DeletedRunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao
}