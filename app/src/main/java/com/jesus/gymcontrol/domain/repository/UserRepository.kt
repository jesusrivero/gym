package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Gym

interface UserRepository {
    suspend fun assignGymToUser(
        uid: String,
        gym: Gym,
        rol: String
    ): Result<Unit>
}