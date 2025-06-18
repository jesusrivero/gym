package com.jesus.gymcontrol.domain.model

import java.util.UUID

data class Pago(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val userCedula: String,
    val membershipId: String,
    val membershipName: String,
    val tipoPago: String,
    val monto: Double,
    val descripcion: String,
    val referencia: String?,
    val fecha: Long = System.currentTimeMillis(),
    val gimnasioCode: String
)