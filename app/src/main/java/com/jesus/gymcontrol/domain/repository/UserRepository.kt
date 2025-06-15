package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.model.GymUserSummary


interface UserRepository {
    suspend fun assignGymToUser(
        uid: String,
        gym: Gym,
        rol: String
    ): Result<Unit>

    suspend fun getGymUserSummary(gymCode:String): Result<GymUserSummary>
}