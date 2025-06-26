package com.jesus.gymcontrol.domain.model.reportModel

data class ReporteMembresia(
	val name: String,
	val price: Double,
	val duracionDias: Double,
	val userCount: Int = 0,
)
