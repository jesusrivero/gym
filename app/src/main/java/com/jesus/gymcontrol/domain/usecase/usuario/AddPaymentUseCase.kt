package com.jesus.gymcontrol.domain.usecase.usuario

import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.repository.PaymentRepository
import javax.inject.Inject

class AddPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(pago: Pago): Result<Unit> {
        return repository.addPago(pago)
    }
}