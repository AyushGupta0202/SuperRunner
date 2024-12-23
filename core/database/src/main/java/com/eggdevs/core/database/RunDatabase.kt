package com.eggdevs.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2
)
abstract class RunDatabase: RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
    abstract val deletedRunPendingSyncDao: DeletedRunPendingSyncDao
    abstract val analyticsDao: AnalyticsDao
}

val Migration_1_2 = object: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE RunEntity ADD COLUMN avgHeartRate INTEGER")
        db.execSQL("ALTER TABLE RunEntity ADD COLUMN maxHeartRate INTEGER")

        // Add new columns to RunPendingSyncEntity table
        db.execSQL("ALTER TABLE RunPendingSyncEntity ADD COLUMN avgHeartRate INTEGER")
        db.execSQL("ALTER TABLE RunPendingSyncEntity ADD COLUMN maxHeartRate INTEGER")
    }
}