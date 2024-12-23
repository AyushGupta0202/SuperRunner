package com.eggdevs.core.database.mappers

import com.eggdevs.core.database.entities.RunEntity
import com.eggdevs.core.domain.location.Location
import com.eggdevs.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        distanceMeters = distanceMeters,
        location = Location(
            lat = latitude,
            long = longitude
        ),
        duration = durationMillis.milliseconds,
        mapPictureUrl = mapPictureUrl,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        totalElevationMeters = totalElevationMeters,
        maxSpeedKmh = maxSpeedKmh,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        mapPictureUrl = mapPictureUrl,
        latitude = location.lat,
        longitude = location.long,
        distanceMeters = distanceMeters,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        durationMillis = duration.inWholeMilliseconds,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun List<RunEntity>.toRuns(): List<Run> {
    return map { it.toRun() }
}

fun List<Run>.toRunEntities(): List<RunEntity> {
    return map { it.toRunEntity() }
}