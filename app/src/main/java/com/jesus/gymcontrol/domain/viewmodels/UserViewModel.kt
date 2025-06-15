package com.jesus.gymcontrol.domain.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.GymRepository
import com.jesus.gymcontrol.domain.usecase.usuario.AssignGymToUserUseCase
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val assignGymToUserUseCase: AssignGymToUserUseCase,
    private val sessionManager: SessionManager,
    private val repository: GymRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun assignGymToUser(
        uid: String,
        gym: Gym,
        rol: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = assignGymToUserUseCase(uid, gym, rol)

            isLoading = false

            result.onSuccess {
                sessionManager.setUserSessionData(uid, rol, gym.code)
                isSuccess = true
                onSuccess()
            }.onFailure {
                errorMessage = it.message
                onError(it.message ?: "Error desconocido")
            }
        }
    }

    fun onConfirmAssignGym(
        gym: Gym?,
        codeInput: String,
        navController: NavController,
        context: Context,
        rol: String = "cliente"
    ) {
        if (gym == null) {
            Toast.makeText(context, "Seleccione un gimnasio", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        assignGymToUser(
            uid = currentUser.uid,
            gym = gym,
            rol = rol,
            onSuccess = {
                Toast.makeText(context, "Gimnasio asignado correctamente", Toast.LENGTH_SHORT).show()
                navController.navigate(AppRoutes.MainScreen) {
                    popUpTo("SelectedGymClient") { inclusive = true }
                }
            },
            onError = { error ->
                Toast.makeText(context, "Error al asignar: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

}