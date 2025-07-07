package com.jesus.gymcontrol.domain.model.notification

import com.google.firebase.Timestamp

data class Notificacion(
	val id: String = "",
	val mensaje: String = "",
	val fecha: Long = 0L,
	val titulo: String = "",
)