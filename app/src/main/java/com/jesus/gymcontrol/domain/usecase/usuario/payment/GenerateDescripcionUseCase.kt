package com.jesus.gymcontrol.domain.usecase.usuario.payment

import com.jesus.gymcontrol.domain.repository.PaymentRepository
import javax.inject.Inject

class GenerarDescripcionPagoUseCase @Inject constructor(
	private val repository: PaymentRepository
) {
	suspend operator fun invoke(
		userId: String,
		gymCode: String,
		nuevaMembresia: String
	): String {
		return repository.generarDescripcionPago(userId, gymCode, nuevaMembresia)
	}
}
