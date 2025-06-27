package com.jesus.gymcontrol.domain.model.reportModel


data class ReportePromocion(
	val nombre: String,
	val descripcion: String,
	val porcentaje: Double,
	val activa: Boolean,
	val duracion: Int = 0,
	val cantidadUsuarios: Int = 0
)