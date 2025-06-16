package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.usecase.usuario.GenerateCodeUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.GetGymByOwnerUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUserFromAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterUserFromAdminViewModel @Inject constructor(
    private val registerUserFromAdminUseCase: RegisterUserFromAdminUseCase,
    private val getGymByOwnerUseCase: GetGymByOwnerUseCase,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    var isRegistering by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var selectedRoleForCode: String = "cliente"

    fun registerUserAsAdmin(
        email: String,
        password: String,
        name: String,
        phone: String,
        idCard: String,
        gender: String,
        age: Int,
        rol: String,
        membership: String,
        code: String,
        gimnasioCode: String
    ) {
        viewModelScope.launch {
            isRegistering = true
            errorMessage = null
            registerSuccess = false

            val result = registerUserFromAdminUseCase(
                email = email,
                password = password,
                name = name,
                phone = phone,
                idCard = idCard,
                gender = gender,
                age = age,
                rol = rol,
                membership = membership,
                code = code,
                gimnasioCode = gimnasioCode
            )

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
}
