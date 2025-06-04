package com.jesus.gymcontrol.domain.models


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUserUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
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
}
