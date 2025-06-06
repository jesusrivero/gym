package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository


class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return repository.registerUser(name, email, password)
    }
}