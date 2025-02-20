package com.eggdevs.wear.run.domain

import com.eggdevs.core.domain.util.Error

enum class ExerciseError: Error {
    TRACKING_NOT_SUPPORTED,
    ONGOING_OWN_EXERCISE,
    ONGOING_OTHER_EXERCISE,
    EXERCISE_ALREADY_ENDED,
    UNKNOWN
}