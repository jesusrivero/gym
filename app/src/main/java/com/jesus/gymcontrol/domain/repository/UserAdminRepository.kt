package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.model.getAdmin.AdminSummary

interface UserAdminRepository {
	suspend fun registerUserFromAdmin(userData: UserRegistrationData):Result<Unit>
	
	suspend fun getAdministradoresByGym(gymCode: String): Result<List<AdminSummary>>
	
	suspend fun updateAdminState(gymCode: String, adminUid: String, newState: String): Result<Unit>
}


