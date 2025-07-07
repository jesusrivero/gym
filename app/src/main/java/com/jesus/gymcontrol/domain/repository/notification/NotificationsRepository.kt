package com.jesus.gymcontrol.domain.repository.notification

import com.jesus.gymcontrol.domain.model.notification.Notificacion
import kotlinx.coroutines.flow.Flow

interface NotificacionRepository {
	suspend fun agregarNotificacion(gymId: String, notificacion: Notificacion)
	fun obtenerNotificaciones(gymCode: String): Flow<List<Notificacion>>
	suspend fun eliminarTodasNotificaciones(gymCode:String)
	suspend fun eliminarNotificacionesAntiguas(gymCode: String, max: Int = 0)

}