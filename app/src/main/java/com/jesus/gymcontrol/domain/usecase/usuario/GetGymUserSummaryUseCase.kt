package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.repository.UserRepository
import javax.inject.Inject

class GetGymUserSummaryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(gymCode: String): Result<GymUserSummary> {
        return userRepository.getGymUserSummary(gymCode)
    }
}