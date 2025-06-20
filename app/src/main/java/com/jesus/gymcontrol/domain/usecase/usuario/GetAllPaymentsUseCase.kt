package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import javax.inject.Inject

class GetAllPaymentsUseCase @Inject constructor(
	private val repository: PaymentRepository
) {
	suspend operator fun invoke(gymCode: String): List<Payment> {
		return repository.getAllPayments(gymCode)
	}
}