package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.usecase.usuario.CreateGymUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GymViewModel @Inject constructor(
    private val createGymUseCase: CreateGymUseCase
) : ViewModel() {

    var name by mutableStateOf("")
    var admin by mutableStateOf("")
    var coach by mutableStateOf("")
    var direction by mutableStateOf("")
    var phone by mutableStateOf("")
    var code by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun createGym() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val name = name.trim()
        val admin = admin.trim()
        val coach = coach.trim()
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
                admin = admin,
                coach = coach,
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
}