package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.usecase.usuario.RegisterUserFromAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterUserFromAdminViewModel @Inject constructor(
    private val registerUserFromAdminUseCase: RegisterUserFromAdminUseCase
) : ViewModel() {

    var isRegistering by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

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
}
