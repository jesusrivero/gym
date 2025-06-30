package com.jesus.gymcontrol.domain.viewmodels.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.notification.Notificacion
import com.jesus.gymcontrol.domain.usecase.usuario.notification.AddNotificacionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.DeleteNotificacionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.GetNotificacionesUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.notification.PurgeNotificacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor(
	private val addNotificacionUseCase: AddNotificacionUseCase,
	private val getNotificacionesUseCase: GetNotificacionesUseCase,
	private val deleteNotificacionUseCase: DeleteNotificacionUseCase,
	private val purgeNotificacionesUseCase: PurgeNotificacionesUseCase,
	private val sessionManager: SessionManager
) : ViewModel() {
	
	private val _notifications = MutableStateFlow<List<Notificacion>>(emptyList())
	val notifications: StateFlow<List<Notificacion>> = _notifications
	
	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean> = _isLoading
	
	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error
	
	init {
		loadNotifications()
	}
	
	
	fun loadNotifications() {
		viewModelScope.launch {
			_isLoading.value = true
			_error.value = null
			try {
				val gymId = sessionManager.getGymCode() ?: ""
				
				getNotificacionesUseCase(gymId).collect { result ->
					_notifications.value = result
					
					// Si supera las 30 notificaciones, purga las más antiguas
					if (result.size > 30) {
						purgeNotifications(gymId)
					}
				}
				
			} catch (e: Exception) {
				_error.value = e.message ?: "Error al cargar notificaciones"
			} finally {
				_isLoading.value =false
			}
		}
	}
	
	fun addNotification(notificacion: Notificacion) {
		viewModelScope.launch {
			try {
				val gymId = sessionManager.getGymCode() ?: throw Exception("Gimnasio no encontrado")
				addNotificacionUseCase(gymId, notificacion)
				loadNotifications() // refrescar lista
			} catch (e: Exception) {
				_error.value = e.message ?: "Error al agregar notificación"
			}
		}
	}
	
	fun deleteNotification(notificacionId: String) {
		viewModelScope.launch {
			try {
				val gymId = sessionManager.getGymCode() ?: throw Exception("Gimnasio no encontrado")
				deleteNotificacionUseCase(gymId, notificacionId)
				loadNotifications() // refrescar lista
			} catch (e: Exception) {
				_error.value = e.message ?: "Error al eliminar notificación"
			}
		}
	}
	
	private fun purgeNotifications(gymId: String) {
		viewModelScope.launch {
			try {
				purgeNotificacionesUseCase(gymId)
				loadNotifications() // refrescar lista luego de purgar
			} catch (e: Exception) {
				_error.value = e.message ?: "Error al purgar notificaciones"
			}
			}
		}
}