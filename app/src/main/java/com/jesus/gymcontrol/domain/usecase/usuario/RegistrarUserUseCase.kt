package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor (
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name:String, email:String, password:String): Result<Unit> {
        return repository.registerUser(name, password, email)
    }
}