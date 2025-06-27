package com.jesus.gymcontrol.domain.model

data class UserProfile(
	val name: String = "",
	val phone: String = "",
	val age: Int? = null,
	val gender: String = "",
	val weight: Double? = null,
	val height: Double?=null
)