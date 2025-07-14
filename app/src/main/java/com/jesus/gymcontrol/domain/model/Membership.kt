package com.jesus.gymcontrol.domain.model

data class Membership(
	val id: String = "",
	val nombre: String = "",
	val cantidadUsuarios: Int = 0,
	val activo: Boolean = true,
	val precio: Double = 0.0,
	val gimnasioCode: String = "",
	val duracionDias: Int = 0,
)
