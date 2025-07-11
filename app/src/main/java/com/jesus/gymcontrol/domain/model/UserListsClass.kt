package com.jesus.gymcontrol.domain.model


data class ListUser(
	val id: String = "",
	val name: String = "",
	val lastname: String = "",
	val idcard: String = "",
	val date: Long? = null,
	val phone: String = "",
	val email: String = "",
	val rol: String = "",
	val enabled: Boolean = false,
	val state: String = "",
)