package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment


interface PaymentRepository {
	suspend fun addPago(pago: Pago): Result<Unit>
	
	
	suspend fun getAllPayments(gymCode: String): List<Payment>
	
	/** Nueva función para calcular la nueva fecha de vencimiento */
	suspend fun calcularNuevaFechaVencimiento(
		userId: String,
		gymCode: String,
		membershipDays: Int
		):Long
	
	suspend fun generarDescripcionPago(
		userId: String,
		gymCode: String,
		nuevaMembresia: String
	): String
}