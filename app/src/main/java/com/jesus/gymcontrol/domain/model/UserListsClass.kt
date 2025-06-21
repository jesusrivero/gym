package com.jesus.gymcontrol.domain.model

data class ListUser(
	val id: String = "",
	val name: String = "",
	val idCard: String = "",
	val phone: String = "",
	val email: String = "",
	val enabled: Boolean = false,
	val state: String = "",
	val lastPaymentDate: Long? = null
)