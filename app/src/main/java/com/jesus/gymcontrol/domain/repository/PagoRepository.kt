package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Pago


interface PagoRepository {
    suspend fun addPago(pago: Pago): Result<Unit>
}