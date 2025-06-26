package com.jesus.gymcontrol.domain.usecase.usuario.validateCode

import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class ValidateAdminCodeUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(code: String, gymCode: String): Result<Boolean> {
        return repository.validateAdminCode(code, gymCode)
    }
}