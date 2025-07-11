package com.jesus.gymcontrol.domain.model.reportModel

data class ReportePago(
	val nombreCliente: String,
	val apellidoCliente: String,
	val cedula: String,
	val membresia: String,
	val tipoPago: String,
	val monto: Double,
	val montoDolar: Double,
	val montoBolivares: Double,
	val fecha: Long,
	val referencia: String?,
	val promocionNombre: String?,
)
