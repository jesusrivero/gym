package com.jesus.gymcontrol.domain.model

data class Promotion(
	val id: String = "",
	val nombre: String = "",
	val descripcion: String = "",
	val porcentajeDescuento: Double = 0.0,
	val duracionDias: Int = 0,
	val gimnasioCode: String = "",
	val activo: Boolean = true,
	val fechaCreacion: Long = System.currentTimeMillis(),
	val fechaVencimiento: Long = System.currentTimeMillis() + duracionDias * 24 * 60 * 60 * 1000L
)