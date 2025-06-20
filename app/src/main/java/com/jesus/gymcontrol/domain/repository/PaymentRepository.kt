package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment


interface PaymentRepository {
	suspend fun addPago(pago: Pago): Result<Unit>
	
	
	suspend fun getAllPayments(gymCode: String): List<Payment>
}