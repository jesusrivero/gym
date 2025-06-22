package com.jesus.gymcontrol.domain.model

data class GetPromotion (
	val id: String = "",
	val nombre: String = "",
	val descripcion: String = "",
	val porcentajeDescuento: Double = 0.0,
	val fechaCreacion: Long = System.currentTimeMillis(),
	val duracionDias: Int = 0,
)
