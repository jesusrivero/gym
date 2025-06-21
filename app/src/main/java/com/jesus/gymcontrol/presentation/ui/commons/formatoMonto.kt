package com.jesus.gymcontrol.presentation.ui.commons

import com.jesus.gymcontrol.domain.model.Payment
import java.text.NumberFormat
import java.util.Locale

fun formatMonto(payment: Payment): String {
	val locale = Locale("es", "VE") // Estilo de Venezuela (latino)
	val formatter = NumberFormat.getNumberInstance(locale).apply {
		minimumFractionDigits = 2
		maximumFractionDigits = 2
	}
	
	return when (payment.paymentType) {
		"Dólares" -> {
			val amount = payment.amountDollar ?: payment.amount
			"$${formatter.format(amount)}"
		}
		
		"Bolívares" -> {
			val amount = payment.amountBs ?: payment.amount
			"Bs. ${formatter.format(amount)}"
		}
		
		"Mixto" -> {
			val dolar = formatter.format(payment.amountDollar ?: 0.0)
			val bs = formatter.format(payment.amountBs ?: 0.0)
			"$$dolar + Bs. $bs"
		}
		
		else -> "${formatter.format(payment.amount)}"
	}
}
