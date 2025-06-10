package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Gym

interface GymRepository {
    suspend fun createGymForUser(
        uid: String,
        code: String,
        name: String,
        admin: String,
        coach: String,
        direction: String,
        phone: String,
    ): Result<Unit>

    suspend fun getAllGyms():
            Result<List<Gym>>
}