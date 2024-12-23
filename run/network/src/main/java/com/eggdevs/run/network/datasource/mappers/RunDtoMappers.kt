package com.eggdevs.run.network.datasource.mappers

import com.eggdevs.core.domain.location.Location
import com.eggdevs.core.domain.run.Run
import com.eggdevs.run.network.datasource.models.CreateRunRequest
import com.eggdevs.run.network.datasource.models.RunDto
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        distanceMeters = distanceMeters,
        duration = durationMillis.milliseconds,
        id = id,
        location = Location(
            lat = lat,
            long = long
        ),
        maxSpeedKmh = maxSpeedKmh,
        mapPictureUrl = mapPictureUrl,
        totalElevationMeters = totalElevationMeters,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunDto(): RunDto {
    return RunDto(
        distanceMeters = distanceMeters,
        durationMillis = duration.inWholeMilliseconds,
        id = id!!,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun CreateRunRequest.toRun(): Run {
    return Run(
        distanceMeters = distanceMeters,
        duration = durationMillis.milliseconds,
        id = id,
        location = Location(
            lat = lat,
            long = long
        ),
        maxSpeedKmh = maxSpeedKmh,
        mapPictureUrl = null,
        totalElevationMeters = totalElevationMeters,
        dateTimeUtc = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.of("UTC")),
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toCreateRunRequest(): CreateRunRequest {
    return CreateRunRequest(
        distanceMeters = distanceMeters,
        durationMillis = duration.inWholeMilliseconds,
        id = id!!,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        epochMillis = dateTimeUtc.toEpochSecond() * 1000L,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun List<RunDto>.toRuns(): List<Run> {
    return map { it.toRun() }
}