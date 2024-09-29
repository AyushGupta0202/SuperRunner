package com.eggdevs.analytics.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.analytics.presentation.dashboard.mappers.toAnalyticsDashboardState
import com.eggdevs.analytics.domain.repository.RunAnalyticsRepository
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    private val runAnalyticsRepository: RunAnalyticsRepository
): ViewModel() {
    var state by mutableStateOf<AnalyticsDashboardState?>(null)
        private set

    init {
        viewModelScope.launch {
            state = runAnalyticsRepository.getAnalyticsValues().toAnalyticsDashboardState()
        }
    }
}