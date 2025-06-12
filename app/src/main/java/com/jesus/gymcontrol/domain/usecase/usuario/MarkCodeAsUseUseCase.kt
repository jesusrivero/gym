package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class MarkCodeAsUseUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(code:String, rol: String? = null):
            Result<Unit> {
        return repository.markCodeAsUsed(code,rol)
    }
}