package com.jesus.gymcontrol.presentation.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RecoverPasswordUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdateRolUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val recoverUseCase: RecoverPasswordUseCase,
    private val updateRolUseCase: UpdateRolUseCase
) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)
    var recoverSuccess by mutableStateOf(false)

    var rol by mutableStateOf("Dueño")
    var codigo by mutableStateOf("")

    fun registerUser(email: String, password: String, name: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = registerUseCase(name.trim(), email.trim(), password.trim())
            isLoading = false
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = loginUseCase(email.trim(), password.trim())
            isLoading = false
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            recoverSuccess = false

            val result = recoverUseCase(email.trim())
            isLoading = false
            result.onSuccess {
                recoverSuccess = true
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    fun clearLoginState() {
        isSuccess = false
        errorMessage = null
    }

    fun assignRoleAndCode(rol: String, codigo: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        if (codigo.isBlank()) {
            errorMessage = "Debes ingresar un código"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isSuccess = false

            val result = updateRolUseCase(uid, rol, codigo)
            isLoading = false
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                errorMessage = it.message
            }
        }
    }
}