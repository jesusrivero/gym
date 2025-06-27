package com.jesus.gymcontrol.domain.model

data class PasswordChangeRequest(
	val currentPassword: String,
	val newPassword: String,
	val confirmPassword: String
)
