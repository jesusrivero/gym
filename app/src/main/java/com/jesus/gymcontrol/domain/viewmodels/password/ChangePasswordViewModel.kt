package com.jesus.gymcontrol.domain.viewmodels.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.model.PasswordChangeRequest
import com.jesus.gymcontrol.domain.usecase.usuario.password.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
	private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {
	
	var isLoading by mutableStateOf(false)
		private set
	var isSuccess by mutableStateOf(false)
		private set
	var errorMessage by mutableStateOf<String?>(null)
	
	
	fun changePassword(request: PasswordChangeRequest) {
		viewModelScope.launch {
			isLoading = true
			isSuccess = false
			errorMessage = null
			
			val result = changePasswordUseCase(request)
			isLoading = false
			result.fold(
				onSuccess = {
					isSuccess = true
				},
				onFailure = {
					errorMessage = it.message ?: "Error al cambiar la contraseña"
				}
				)
			}
	}
	
	fun resetState() {
		isLoading = false
		isSuccess = false
		errorMessage = null
	}
}
