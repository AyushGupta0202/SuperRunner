package com.eggdevs.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.run.presentation.run_overview.mappers.toRunUIs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewModel(
    private val runRepository: RunRepository
): ViewModel() {

    var state by mutableStateOf(
        RunOverviewState()
    )

    init {
        runRepository
            .getRuns()
            .onEach { runs ->
                state = state.copy(
                    runs = runs.toRunUIs()
                )
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when(action) {
            RunOverviewAction.OnAnalyticsClick -> {}
            RunOverviewAction.OnLogoutClick -> {}
            RunOverviewAction.OnStartRunClick -> {}
            is RunOverviewAction.OnDeleteRunClick -> {
                viewModelScope.launch {
                    runRepository.deleteRun(action.runUi.id)
                }
            }
            else -> Unit
        }
    }
}