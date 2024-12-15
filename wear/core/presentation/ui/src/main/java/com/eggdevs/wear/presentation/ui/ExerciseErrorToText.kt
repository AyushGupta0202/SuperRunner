package com.eggdevs.wear.presentation.ui

import com.eggdevs.core.presentation.ui.UiText
import com.eggdevs.wear.run.domain.ExerciseError

// TODO: Move the presentation.designsystem_wear to the wear core module and create new presentation.ui module
fun ExerciseError.toUiText(): UiText? {
    return when(this) {
        ExerciseError.ONGOING_OWN_EXERCISE,
        ExerciseError.ONGOING_OTHER_EXERCISE -> {
            UiText.StringResource(R.string.error_ongoing_exercise)
        }
        ExerciseError.EXERCISE_ALREADY_ENDED -> {
            UiText.StringResource(R.string.error_exercise_already_ended)
        }
        ExerciseError.UNKNOWN -> {
            UiText.StringResource(com.eggdevs.core.presentation.ui.R.string.error_unknown)
        }
        ExerciseError.TRACKING_NOT_SUPPORTED -> null
    }
}