package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.GymRepository
import javax.inject.Inject

class GetGymByOwnerUseCase @Inject constructor(
    private val repository: GymRepository
) {
    suspend operator fun invoke(ownerUid: String): Result<Gym> {
        return repository.getGymByOwnerUid(ownerUid)
    }
}