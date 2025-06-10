package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.AuthRepository
import javax.inject.Inject


class UpdateDatesUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        uid: String,
        cedula: String,
        edad: String,
        numero: String,
        sexo: String
    ): Result<Unit> {
        return repository.updateDatesUser(uid, cedula, edad, numero, sexo)
    }
}