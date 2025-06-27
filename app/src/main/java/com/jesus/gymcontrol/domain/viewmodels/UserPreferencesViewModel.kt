package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.UserProfile
import com.jesus.gymcontrol.domain.model.UserUpdate
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
	private val updateUserProfileUseCase: UpdateUserProfileUseCase,
	private val sessionManager: SessionManager,
	private val firestore: FirebaseFirestore
) : ViewModel() {
	
	var isLoading by mutableStateOf(false)
		private set
	var errorMessage by mutableStateOf<String?>(null)
	var isSuccess by mutableStateOf(false)
	
	var userProfile by mutableStateOf<UserProfile?>(null)
		private set
	
	
	fun updateProfile(userUpdate: UserUpdate) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			isSuccess = false
			
			if (userUpdate.name.isBlank()) {
				errorMessage = "El nombre no puede estar vacíos"
				isLoading = false
				return@launch
			}
			
			val uid = sessionManager.getUserUid() ?: return@launch
			val gymCode = sessionManager.getGymCode() ?: return@launch
			
			val result = updateUserProfileUseCase(uid, gymCode, userUpdate)
			isLoading = false
			
			result.fold(
				onSuccess = { isSuccess = true },
				onFailure = { e -> errorMessage = e.message}
			)
		}
	}
	
	fun loadUserProfile() {
		viewModelScope.launch {
			val uid = sessionManager.getUserUid() ?: return@launch
			val snapshot = firestore.collection("users").document(uid).get().await()
			
			snapshot?.data?.let { data ->
				userProfile = UserProfile(
					name = data["name"] as? String ?: "",
					phone = data["phone"] as? String ?: "",
					age = (data["age"] as? Long)?.toInt(),
					gender = data["gender"] as? String ?: "",
					weight = (data["weight"] as? Number)?.toDouble(),
					height = (data["height"] as? Number)?.toDouble()
				)
			}
		}
	}
	
	fun resetState() {
		isLoading = false
		isSuccess = false
		errorMessage = null
	}
}
