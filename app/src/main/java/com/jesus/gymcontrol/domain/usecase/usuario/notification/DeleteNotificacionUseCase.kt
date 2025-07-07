package com.jesus.gymcontrol.domain.usecase.usuario.notification

import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import javax.inject.Inject


class DeleteAllNotificacionesUseCase @Inject constructor(
	private val repository: NotificacionRepository
) {
	suspend operator fun invoke(gymCode: String) {
		repository.eliminarNotificacionesAntiguas(gymCode, max = 0) // Borra todas
	}
}
