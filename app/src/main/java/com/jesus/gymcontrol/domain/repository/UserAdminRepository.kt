package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.UserRegistrationData

interface UserAdminRepository {
	suspend fun registerUserFromAdmin(userData: UserRegistrationData):Result<Unit>
}


