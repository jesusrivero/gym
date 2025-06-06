package com.jesus.gymcontrol.domain.models


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun registerUser(email:String, password:String, name:String) {
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

    fun loginUser(email:String, password: String) {
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
}
