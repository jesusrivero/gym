package com.jesus.gymcontrol.domain.model.report

data class ReportePago(
	val nombreCliente: String,
	val cedula: String,
	val membresia: String,
	val tipoPago: String,
	val monto: Double,
	val fecha: Long,
	val referencia: String?,
	val promocionNombre: String?,
	val descuentoAplicado: Double?
)
