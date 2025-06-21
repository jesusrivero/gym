package com.jesus.gymcontrol.domain.model

data class PaymentState(
	val frequency: String = "",
	val type: String = "",
	val amountDollar: String = "",
	val amountBs: String = "",
	val listMembership: Map<String, Double> = emptyMap()  // aquí minúscula inicial
)