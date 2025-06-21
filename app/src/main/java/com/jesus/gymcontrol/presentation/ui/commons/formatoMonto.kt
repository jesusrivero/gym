package com.jesus.gymcontrol.presentation.ui.commons

import com.jesus.gymcontrol.domain.model.Payment
import java.text.NumberFormat
import java.util.Locale

fun formatMonto(payment: Payment): String {
	val locale = Locale("es", "VE")
	val formatter = NumberFormat.getNumberInstance(locale).apply {
		minimumFractionDigits = 2
		maximumFractionDigits = 2
	}
	
	return when (payment.paymentType) {
		"Dólares" -> {
			val amount = payment.amountDollar?.takeIf { it > 0 } ?: payment.amount ?: 0.0
			"$${formatter.format(amount)}"
		}
		
		"Bolívares" -> {
			val amount = payment.amountBs?.takeIf { it > 0 } ?: payment.amount ?: 0.0
			"Bs. ${formatter.format(amount)}"
		}
		
		"Mixto" -> {
			val dolar = payment.amountDollar?.takeIf { it > 0 } ?: 0.0
			val bs = payment.amountBs?.takeIf { it > 0 } ?: 0.0
			"$${formatter.format(dolar)} + Bs. ${formatter.format(bs)}"
		}
		
		else -> {
			val amount = payment.amount ?: 0.0
			formatter.format(amount)
		}
	}
}