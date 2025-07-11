package com.jesus.gymcontrol.domain.usecase.usuario.auth

import com.jesus.gymcontrol.domain.repository.AuthRepository


class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        lastname: String,
        email: String,
        password: String,
        idcard:String):Result<Unit> {
        return repository.registerUser(name,lastname, email, password, idcard)
    }
}