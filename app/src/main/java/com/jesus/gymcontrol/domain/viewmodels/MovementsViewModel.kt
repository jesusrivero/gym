package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetAllPaymentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MovementsViewModel @Inject constructor(
	private val getAllPaymentsUseCase: GetAllPaymentsUseCase,
	private val sessionManager: SessionManager
) : ViewModel() {
	
	var lastPayments by mutableStateOf<List<Payment>>(emptyList())
		private set
	
	var isLoading by mutableStateOf(false)
		private set
	
	init {
		loadLastPayments()
	}
	
	private fun loadLastPayments() {
		val gymCode = sessionManager.getGymCode() ?: return
		viewModelScope.launch {
			isLoading = true
			try {
				val allPayments = getAllPaymentsUseCase(gymCode)
				lastPayments = allPayments
					.sortedByDescending { it.date } // Más recientes primero
					.take(6) // Solo los 6 últimos
			} catch (e: Exception) {
				Log.e("MovementsViewModel", "Error loading payments", e)
			}
			isLoading = false
		}
	}
}