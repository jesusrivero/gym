package com.jesus.gymcontrol.domain.usecase.usuario.notification

import com.jesus.gymcontrol.domain.repository.notification.NotificacionRepository
import javax.inject.Inject

class PurgeNotificacionesUseCase @Inject constructor(
	private val repository: NotificacionRepository
) {
	suspend operator fun invoke(gymId: String, max: Int = 30) {
		repository.eliminarNotificacionesAntiguas(gymId, max)
	}
}
