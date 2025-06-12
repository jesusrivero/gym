package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class GenerateCodeUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(gymCode: String):
            Result<String> {
        return repository.generateCode(gymCode)

    }
}