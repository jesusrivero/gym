package com.jesus.gymcontrol.domain.usecase.usuario.notification

import com.jesus.gymcontrol.domain.model.notification.Notificacion
import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import javax.inject.Inject

class AddNotificacionUseCase @Inject constructor(
	private val repository: NotificacionRepository
) {
	suspend operator fun invoke(gymId: String, notificacion: Notificacion) {
		repository.agregarNotificacion(gymId, notificacion)
	}
}