package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.model.GymUserSummary
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.model.UserUpdate


interface UserRepository {
    suspend fun assignGymToUser(
        uid: String,
        gym: Gym,
        rol: String
    ): Result<Unit>

    suspend fun getGymUserSummary(gymCode:String): Result<GymUserSummary>

    suspend fun getUserByGym(gymCode: String):
        Result<List<ListUser>>
	
	suspend fun updateUserProfile(
		uid: String,
		gymCode: String,
		userUpdate: UserUpdate
	): Result<Unit>
	
	suspend fun searchUserByIdCard(gymCode: String, query: String): Result<List<ListUser>>
	
}