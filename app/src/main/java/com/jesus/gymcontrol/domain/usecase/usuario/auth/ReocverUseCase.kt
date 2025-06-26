package com.jesus.gymcontrol.domain.usecase.usuario.auth

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.recoverPassword(email)
    }
}