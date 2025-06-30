package com.jesus.gymcontrol.domain.usecase.usuario.notification

import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import javax.inject.Inject


class DeleteNotificacionUseCase @Inject constructor(
	private val repository: NotificacionRepository
) {
	suspend operator fun invoke(gymId: String, notificacionId: String) {
		repository.eliminarNotificacion(gymId, notificacionId)
	}
}
