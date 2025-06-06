package com.jesus.gymcontrol.domain.models

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.trim

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
					Log.d("LoginViewModel", "Intentando iniciar sesión: $email | , $password")
            val result = loginUseCase(email.trim(), password.trim())
            _loginState.value = result
        }
    }

    fun clearState() {
        _loginState.value = null
    }
}