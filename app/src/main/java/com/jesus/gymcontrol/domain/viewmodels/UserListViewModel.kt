package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.usecase.usuario.GetUserByGymUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersByGymUseCase: GetUserByGymUseCase,
    private val sessionManager: SessionManager // Para obtener el código del gimnasio
) : ViewModel() {

    var listUsers by mutableStateOf<List<ListUser>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadUsers() {
        val gymCode = sessionManager.getGymCode() ?: return
        viewModelScope.launch {
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
}