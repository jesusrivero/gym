package com.jesus.gymcontrol.domain.model.notification


data class Notificacion(
	val id: String = "",
	val mensaje: String = "",
	val fecha: Long = 0L,
	val titulo: String = "",
	val leido: Boolean = false,
)