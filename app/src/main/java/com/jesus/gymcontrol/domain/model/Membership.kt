package com.jesus.gymcontrol.domain.model

data class Membership(
	val id: String = "",
	val nombre: String = "",
	val state: String = "activo",
	val precio: Double = 0.0,
	val gimnasioCode: String = "",
	val duracionDias: Int = 0,
)
