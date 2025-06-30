package com.jesus.gymcontrol.domain.model.notification

import com.google.firebase.Timestamp


data class Notificacion(
	val id: String = "",
	val mensaje: String = "",
	val tipo: String = "",
	val fecha: Timestamp = Timestamp.now(),
	val paraRol: String = "",
	val usuarioRelacionado: String?=null
)