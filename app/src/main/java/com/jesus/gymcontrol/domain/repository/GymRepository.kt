package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Gym

interface GymRepository {
    suspend fun createGymForUser(
        uid: String,
        code: String,
        name: String,
        direction: String,
        phone: String,
    ): Result<Unit>

    suspend fun getAllGyms():
            Result<List<Gym>>

    suspend fun validateGymCode(code:String):
        Result<Boolean>

    suspend fun markCodeAsUsed(
        code: String):
            Result<Unit>

}