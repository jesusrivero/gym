package com.jesus.gymcontrol.domain.model


data class Pago(
	val id: String = "",
	val userId: String = "",
	val name: String = "",
	val idcard: String = "",
	val membershipId: String = "",
	val membershipName: String = "",
	val tipepayment: String = "",
	val promocionId: String? = null,
	val promocionNombre: String? = null,
	val promocionDescripcion: String? = null,
	val promocionDescuento: Double? = null,
	val amount: Double = 0.0,
	val amountDollar: Double? = null,
	val amountBs: Double? = null,
	val description: String? = null,
	val reference: String? = null,
	val date: Long = System.currentTimeMillis(),
	val gimnasioCode:String=""
)