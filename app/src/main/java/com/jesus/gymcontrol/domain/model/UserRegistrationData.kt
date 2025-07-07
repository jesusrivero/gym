package com.jesus.gymcontrol.domain.model

data class UserRegistrationData(
	val email: String,
	val password: String,
	val name: String,
	val phone: String?,
	val idCard: String,
	val gender: String,
	val age: Int,
	val rol: String,
	val membership: String,
	val code: String,
	val gimnasioCode: String,
	val date: Long? = null
)