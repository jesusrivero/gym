package com.jesus.gymcontrol.domain.usecase.usuario.auth

import com.jesus.gymcontrol.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.loginUser(email, password)
    }
}