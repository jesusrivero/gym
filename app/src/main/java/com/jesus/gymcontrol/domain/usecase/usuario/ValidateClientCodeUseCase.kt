package com.jesus.gymcontrol.domain.usecase.usuario


import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class ValidateClientCodeUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(code: String, gymCode: String):
            Result<Boolean> {
        return repository.validateClientCode(code, gymCode)
    }
}