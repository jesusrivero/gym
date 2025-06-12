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

    suspend fun validateOwnerCode(code:String):
        Result<Boolean>

    suspend fun validateClientCode(code:String, gymCode: String):
            Result<Boolean>

    suspend fun markCodeAsUsed(code: String, rol:String?):
            Result<Unit>



    suspend fun generateCode(gymCode: String):
            Result<String>
    fun generateRandomCode(length: Int): String

    suspend fun getGymByOwnerUid(uid:String):
            Result<Gym>
}