package com.jesus.gymcontrol.domain.model

data class Payment(
	val id: String = "",
	val name: String = "",
	val idCard: String = "",
	val membership: String = "",
	val typePayment: String = "",
	val amount: Double = 0.0,
	val reference: String = "",
	val description: String = "",
	val date: Long = 0L // Epoch millis
)