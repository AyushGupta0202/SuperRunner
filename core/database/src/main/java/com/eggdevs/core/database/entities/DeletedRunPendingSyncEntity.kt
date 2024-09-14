package com.eggdevs.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeletedRunPendingSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val runId: String,
    val userId: String
)
