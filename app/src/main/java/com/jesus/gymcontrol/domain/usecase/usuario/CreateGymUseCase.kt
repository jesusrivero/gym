package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.repository.GymRepository

class CreateGymUseCase(private val repository: GymRepository
) {
    suspend operator fun invoke(
        uid:String,
        code:String,
        name:String,
        admin:String,
        coach:String,
        direction:String,
        phone:String,
    ): Result<Unit> {
        return repository.createGymForUser(uid, code, name, admin, coach, direction, phone)
    }
}