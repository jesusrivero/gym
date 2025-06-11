package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class ValidateCodeUseCase @Inject constructor(
    private val gymRepository: GymRepository
) {
    suspend operator fun invoke(code:String):
            Result<Boolean>{
        return gymRepository.validateGymCode(code)
    }
}