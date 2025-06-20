package com.jesus.gymcontrol.domain.model

import java.util.UUID

data class Pago(
	val id: String = UUID.randomUUID().toString(),
	val userId: String,
	val name: String,
	val idcard: String,
	val membershipId: String,
	val membershipName: String,
	val tipepayment: String,
	val amount: Double,
	val description: String,
	val reference: String?,
	val date: Long = System.currentTimeMillis(),
	val gimnasioCode: String
)