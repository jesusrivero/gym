package com.jesus.gymcontrol.domain.usecase.usuario.notification

import com.jesus.gymcontrol.domain.model.notification.Notificacion
import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificacionesUseCase @Inject constructor(
	private val repository: NotificacionRepository
) {
	operator fun invoke(gymId: String): Flow<List<Notificacion>> {
		return repository.obtenerNotificaciones(gymId)
	}
}