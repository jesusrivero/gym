package com.jesus.gymcontrol.domain.repository


interface AuthRepository {
	suspend fun registerUser(
		name: String,
		email: String,
		password: String,
	): Result<Unit>
	
	
	suspend fun loginUser(
		email: String,
		password: String,
	): Result<Unit>
}

