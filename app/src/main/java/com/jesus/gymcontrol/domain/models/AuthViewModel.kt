package com.jesus.gymcontrol.domain.models


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jesus.gymcontrol.domain.usecase.usuario.LoginUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
	private val registerUserUseCase: RegisterUserUseCase,
	private val loginUseCase: LoginUseCase,
	
	) : ViewModel() {
	
	private val _registerState = MutableStateFlow<Result<Unit>?>(null)
	val registerState: StateFlow<Result<Unit>?> = _registerState
	
	fun registerUser(email: String, password: String, name: String) {
		viewModelScope.launch {
			val result = registerUserUseCase(email, password, name)
			_registerState.value = result
		}
	}
	
	fun clearState() {
		_registerState.value = null
	}
	
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
	
	fun clearStateLogin() {
		_loginState.value = null
	}
	
	
}
