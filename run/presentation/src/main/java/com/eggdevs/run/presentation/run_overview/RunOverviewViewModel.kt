package com.eggdevs.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.core.domain.SessionStorage
import com.eggdevs.core.domain.run.SyncRunScheduler
import com.eggdevs.core.domain.run.repository.RunRepository
import com.eggdevs.run.presentation.run_overview.mappers.toRunUIs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class RunOverviewViewModel(
    private val runRepository: RunRepository,
    private val syncRunScheduler: SyncRunScheduler,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
): ViewModel() {

    var state by mutableStateOf(
        RunOverviewState()
    )

    init {
        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                type = SyncRunScheduler.SyncType.FetchRuns(30.minutes)
            )
        }
        runRepository
            .getRuns()
            .onEach { runs ->
                state = state.copy(
                    runs = runs.toRunUIs()
                )
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            runRepository.syncPendingRuns()
            runRepository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when(action) {
            RunOverviewAction.OnAnalyticsClick -> {}
            RunOverviewAction.OnLogoutClick -> {
                logout()
            }
            RunOverviewAction.OnStartRunClick -> {}
            is RunOverviewAction.OnDeleteRunClick -> {
                viewModelScope.launch {
                    runRepository.deleteRun(action.runUi.id)
                }
            }
            else -> Unit
        }
    }

    private fun logout() {
        applicationScope.launch {
            syncRunScheduler.cancelAllSyncs()
            runRepository.deleteAllRuns()
            runRepository.logout() // we just want to invalidate the user's token remotely. we don't care about the result. and if the API fails, the token is invalidated anyways after an hour.
            sessionStorage.setInfo(null) // remove the current users token from the local session storage
        }
    }
}