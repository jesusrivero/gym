package com.jesus.gymcontrol.domain.repository

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

}