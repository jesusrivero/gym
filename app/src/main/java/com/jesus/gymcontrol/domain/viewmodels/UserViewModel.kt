package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.usecase.usuario.AssignGymToUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val assignGymToUserUseCase: AssignGymToUserUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun assignGymToUser(
        uid: String,
        gym: Gym,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = assignGymToUserUseCase(uid, gym)

            isLoading = false

            result.onSuccess {
                isSuccess = true
                onSuccess()
            }.onFailure {
                errorMessage = it.message
                onError(it.message ?: "Error desconocido")
            }
        }
    }
}