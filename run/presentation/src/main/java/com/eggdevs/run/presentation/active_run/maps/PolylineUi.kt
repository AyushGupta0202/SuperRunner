package com.eggdevs.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import com.eggdevs.core.domain.location.Location

data class PolylineUi(
    val locationA: Location,
    val locationB: Location,
    val color: Color
)
