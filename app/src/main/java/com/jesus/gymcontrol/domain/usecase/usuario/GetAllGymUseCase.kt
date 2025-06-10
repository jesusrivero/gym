package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.repository.GymRepository

class GetAllGymUseCase (
    private val repository: GymRepository
) {
    suspend operator fun invoke(): Result<List<Gym>>  {
        return repository.getAllGyms()
    }
}