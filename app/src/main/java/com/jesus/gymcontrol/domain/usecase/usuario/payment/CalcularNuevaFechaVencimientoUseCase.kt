package com.jesus.gymcontrol.domain.usecase.usuario.payment

import com.jesus.gymcontrol.domain.repository.PaymentRepository
import javax.inject.Inject

class CalcularNuevaFechaVencimientoUseCase @Inject constructor(
	private val paymentRepository: PaymentRepository
) {
	suspend operator fun invoke(
		userId: String,
		gymCode: String,
		membershipDays: Int
	): Long {
		return paymentRepository.calcularNuevaFechaVencimiento(userId, gymCode, membershipDays)
		}
}