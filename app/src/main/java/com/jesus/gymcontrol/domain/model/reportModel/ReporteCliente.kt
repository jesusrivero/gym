package com.jesus.gymcontrol.domain.model.reportModel

data class ReporteCliente(
	val nombre: String,
	val apellido: String,
	val cedula: String,
	val correo: String,
	val telefono: String,
	val activo: String,
	val date: Long
)
