package com.jesus.gymcontrol.domain.model

data class UserUpdate(
	val name: String,
	val lastname: String,
	val phone: String? = null,
	val age: Int? = null,
	val gender: String? = null,
	val weight: Double? = null,
	val height: Double?=null
)