package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.model.getAdmin.AdminSummary
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetGymByOwnerUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.auth.RegisterUserFromAdminUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getAdmin.GetGymAdminsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getAdmin.UpdateAdminStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterUserFromAdminViewModel @Inject constructor(
    private val registerUserFromAdminUseCase: RegisterUserFromAdminUseCase,
    private val getGymByOwnerUseCase: GetGymByOwnerUseCase,
    private val updateAdminStateUseCase: UpdateAdminStateUseCase,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val getGymAdminsUseCase: GetGymAdminsUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
	
	private val _admins = MutableStateFlow<List<AdminSummary>>(emptyList())
	val admins: StateFlow<List<AdminSummary>> = _admins.asStateFlow()
	
	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
	
	
	
	var isRegistering by mutableStateOf(false)
		private set
	
	var registerSuccess by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	private var selectedRoleForCode: String = "cliente"
	
	fun registerUserAsAdmin(userData: UserRegistrationData) {
		viewModelScope.launch {
			isRegistering = true
			errorMessage = null
			registerSuccess = false
			
			val result = registerUserFromAdminUseCase(userData)
			
			result.onSuccess {
				registerSuccess = true
			}.onFailure {
				errorMessage = it.message
			}
			
			isRegistering = false
		}
	}
	
	fun resetRegisterState() {
		registerSuccess = false
		errorMessage = null
	}
	
	fun SetSelectedRoleForCode(role: String) {
		selectedRoleForCode = role
	}
	
	fun generateCodeForRoleFromAdmin(onCodeGenerated: (String) -> Unit) {
		viewModelScope.launch {
			isRegistering = true
			val uid = firebaseAuth.currentUser?.uid ?: return@launch
			
			val result = getGymByOwnerUseCase(uid)
			result.onSuccess { gym ->
				val gymCode = gym.code
				val codeResult = generateCodeUseCase(gymCode, selectedRoleForCode)
				codeResult.onSuccess { code ->
					onCodeGenerated(code)
				}.onFailure {
					errorMessage = it.message
				}
			}.onFailure {
				errorMessage = it.message
			}
			
			isRegistering = false
		}
	}
	
	
	
	fun loadAdmins() {
		viewModelScope.launch {
			_isLoading.value = true
			errorMessage = null
			
			val uid = firebaseAuth.currentUser?.uid
			if (uid == null) {
				errorMessage = "Usuario no autenticado"
				_isLoading.value = false
				return@launch
			}
			
			val gymResult = getGymByOwnerUseCase(uid)
			gymResult.onSuccess { gym ->
				val gymCode = gym.code
				fetchAdminsForGym(gymCode)
			}.onFailure {
				errorMessage = it.message
				_isLoading.value = false
			}
		}
		
	}
	
	private suspend fun fetchAdminsForGym(gymCode: String) {
		val result = getGymAdminsUseCase(gymCode)
		result.onSuccess { adminsList ->
			_admins.value = adminsList
		}.onFailure { e ->
			errorMessage = e.message
		}
		
		_isLoading.value = false
	}
	
	
	
	fun updateAdminState(admin: AdminSummary, newState: String) {
		viewModelScope.launch {
			_isLoading.value = true
			errorMessage = null
			
			val gymResult = firebaseAuth.currentUser?.uid?.let { getGymByOwnerUseCase(it) }
			gymResult?.onSuccess { gym ->
				val updateResult = updateAdminStateUseCase(gym.code, admin.uid, newState)
				updateResult.onSuccess {
					registerSuccess = true
					loadAdmins()
				}.onFailure {
					errorMessage = it.message
				}
			}?.onFailure {
				errorMessage = it.message
			}
			
			_isLoading.value = false
		}
	}
	
	
	
	fun clearError() {
		errorMessage = null
	}
	
}
