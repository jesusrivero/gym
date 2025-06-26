package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetGymUserSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getGymUserSummaryUseCase: GetGymUserSummaryUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    var summary by mutableStateOf(GymUserSummary())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadGymUserSummary() {
        val gymCode = sessionManager.getGymCode()
        if (gymCode.isNullOrEmpty()) {
            errorMessage = "Código de gimnasio no encontrado"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = getGymUserSummaryUseCase(gymCode)

            result.onSuccess {
                summary = it
            }.onFailure {
                errorMessage = it.message
            }

            isLoading = false
        }
    }
}