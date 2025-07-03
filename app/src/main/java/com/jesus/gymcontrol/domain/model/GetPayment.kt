package com.jesus.gymcontrol.domain.model

data class Payment(
	val id: String = "",
	val userId: String = "",
	val name: String = "",
	val idCard: String = "",
	val membershipId: String = "",
	val membershipName: String = "",
	val paymentType: String = "",
	val amount: Double = 0.0,
	val amountDollar: Double = 0.0,
	val amountBs: Double = 0.0,
	val description: String = "",
	val reference: String? = null,
	val date: Long = 0L,
	val fechaVencimiento: Long = 0L,
	val gymCode: String = "",
	val promocionNombre: String? = null,
	val promocionPorcentajeDescuento: Double? = null,
)