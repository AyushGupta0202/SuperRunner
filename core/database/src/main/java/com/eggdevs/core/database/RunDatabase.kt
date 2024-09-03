package com.eggdevs.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eggdevs.core.database.dao.RunDao
import com.eggdevs.core.database.entities.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class RunDatabase: RoomDatabase() {
    abstract val runDao: RunDao
}