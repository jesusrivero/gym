package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.usecase.usuario.SearchUserUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetUserByGymUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
	private val getUsersByGymUseCase: GetUserByGymUseCase,
	private val searchUserByIdCardUseCase: SearchUserUseCase,
	private val sessionManager: SessionManager
) : ViewModel() {
	
	var listUsers by mutableStateOf<List<ListUser>>(emptyList())
		private set
	
	var isLoading by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	private var searchJob: Job? = null // 👈 para cancelar búsquedas anteriores
	
	fun loadUsers() {
		val gymCode = sessionManager.getGymCode() ?: return
		searchJob?.cancel() // 👈 cancela si hay otra búsqueda en curso
		
		searchJob = viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = getUsersByGymUseCase(gymCode)
			
			result.onSuccess {
				listUsers = it
			}.onFailure {
				errorMessage = it.message ?: "Error desconocido"
			}
			
			isLoading = false
		}
	}
	
	fun searchUser(idCard: String) {
		val gymCode = sessionManager.getGymCode() ?: return
		searchJob?.cancel() // 👈 cancela la búsqueda anterior
		
		if (idCard.isBlank()) {
			loadUsers()
			return
		}
		
		searchJob = viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = searchUserByIdCardUseCase(gymCode, idCard)
			
			result.onSuccess {
				listUsers = it
			}.onFailure {
				errorMessage = it.message ?: "Error desconocido"
			}
			
			isLoading = false
		}
	}
	
	fun clearSearch() {
		listUsers = emptyList()
	}
}