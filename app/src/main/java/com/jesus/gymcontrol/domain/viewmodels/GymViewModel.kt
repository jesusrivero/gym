package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetAllGymUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymViewModel @Inject constructor(
    private val createGymUseCase: CreateGymUseCase,
    private val getAllGymUseCase: GetAllGymUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    var gyms by mutableStateOf<List<Gym>> (emptyList())
    var searchQuery by mutableStateOf("")
    var name by mutableStateOf("")
    var direction by mutableStateOf("")
    var phone by mutableStateOf("")
    var code by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun createGym() {
        val uid = firebaseAuth.currentUser?.uid ?: return

        val name = name.trim()
        val direction = direction.trim()
        val phone = phone.trim()
        val code = code.trim()

        viewModelScope.launch {
            isLoading = true
            isSuccess = false
            errorMessage = null

            val result = createGymUseCase(
                uid = uid,
                code = code,
                name = name,
                direction = direction,
                phone = phone
            )

            isLoading = false
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun fetchAllGyms() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            Log.d("GymViewModel", "Iniciando fetchAllGyms...")

            val result = getAllGymUseCase()
            result.onSuccess {
                gyms = it
                Log.d("GymViewModel", "Gimnasios obtenidos: ${it.size}")
                it.forEach { gym ->
                    Log.d("GymViewModel", "Gym: ${gym.name}, código: ${gym.code}")
                }
            }.onFailure {
                errorMessage = it.message
                Log.e("GymViewModel", "Error al obtener gimnasios: ${it.message}", it)
            }

            isLoading = false
            Log.d("GymViewModel", "Finalizó fetchAllGyms. isLoading = $isLoading")
        }
    }

    fun filteredGyms(): List<Gym> {
        return gyms.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

}