package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.usecase.usuario.CreatePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.DeletePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetPromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetUsersCountByPromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.UpdatePromotionUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.promotion.TogglePromotionStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionViewModel @Inject constructor(
	private val createPromotionUseCase: CreatePromotionUseCase,
	private val getPromotionsUseCase: GetPromotionUseCase,
	private val updatePromotionUseCase: UpdatePromotionUseCase,
	private val deletePromotionUseCase: DeletePromotionUseCase,
	private val getUsersCountByPromotionUseCase: GetUsersCountByPromotionUseCase,
	private val togglePromotionStateUseCase: TogglePromotionStateUseCase,
	private val sessionManager: SessionManager
) : ViewModel() {
	
	var isLoading by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		internal set
	
	var promotions by mutableStateOf<List<Promotion>>(emptyList())
		private set
	
	var userCountByPromotion by mutableStateOf<Map<String, Int>>(emptyMap())
		private set
	
	var precioBaseMembresia by mutableStateOf(0.0)
		private set
	
	var tipoPagoSeleccionado by mutableStateOf("Dólares")
		private set
	
	var promocionSeleccionada by mutableStateOf<Promotion?>(null)
		private set
	
	var montoCalculado by mutableStateOf(0.0)
		private set
	
	private val _promotionActionMessage = mutableStateOf<String?>(null)
	val promotionActionMessage: State<String?> = _promotionActionMessage
	
	private val _isActionSuccess = MutableStateFlow<Boolean?>(null)
	val isActionSuccess = _isActionSuccess.asStateFlow()
	
	// NUEVO: Registrar acción como éxito o error
	private fun setActionResult(message: String, success: Boolean) {
		_promotionActionMessage.value = message
		_isActionSuccess.value = success
	}
	
	fun createPromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = createPromotionUseCase(promotion)
			isLoading = false
			
			result.onSuccess {
				loadPromotions()
			}.onFailure {
				setActionResult("Error al crear promoción: ${it.message}", false)
			}
		}
	}
	
	fun togglePromotionState(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = togglePromotionStateUseCase(promotion)
			isLoading = false
			
			result.onSuccess {
				_promotionActionMessage.value = if (promotion.activo) {
					"Promoción desactivada correctamente"
				} else {
					"Promoción activada correctamente"
				}
				_isActionSuccess.value = true
				loadPromotions() // <--- Recargar lista tras cambio
			}.onFailure {
				errorMessage = it.message
				_promotionActionMessage.value = "Error: ${it.message}"
				_isActionSuccess.value =false
			}
		}
	}
	
	fun updatePromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = updatePromotionUseCase(promotion)
			isLoading = false
			
			result.onSuccess {
				setActionResult("Promoción editada correctamente", true)
				loadPromotions()
			}.onFailure {
				setActionResult("Error al editar promoción: ${it.message}", false)
			}
		}
	}
	
	fun deletePromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			val result = deletePromotionUseCase(promotion)
			isLoading = false
			
			result.onSuccess {
				setActionResult("Promoción eliminada correctamente", true)
				loadPromotions()
			}.onFailure {
				setActionResult("Error al eliminar promoción: ${it.message}", false)
			}
		}
	}
	
	fun loadPromotions() {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = getPromotionsUseCase()
			isLoading = false
			
			result.onSuccess {
				promotions = it
			}.onFailure {
				errorMessage = it.message
			}
		}
	}
	
	fun loadUserCountByPromotion() {
		val gymCode = sessionManager.getGymCode() ?: return
		viewModelScope.launch {
			val result = getUsersCountByPromotionUseCase(gymCode)
			result.onSuccess {
				userCountByPromotion = it
			}.onFailure {
				Log.e("PromotionViewModel", "Error obteniendo conteo de usuarios", it)
			}
		}
	}
	
	fun setTipoPago(tipo: String) {
		tipoPagoSeleccionado = tipo
		recalcularMontoFinal()
	}
	
	fun setPromocion(promocion: Promotion?) {
		promocionSeleccionada = promocion
		recalcularMontoFinal()
	}
	
	fun setPrecioBase(precio: Double) {
		precioBaseMembresia = precio
		recalcularMontoFinal()
	}
	
	private fun recalcularMontoFinal() {
		montoCalculado = calcularMontoConPromocion(
			precioMembresia = precioBaseMembresia,
			promocion = promocionSeleccionada,
			tipoPago = tipoPagoSeleccionado
		)
	}
	
	fun calcularMontoConPromocion(
		precioMembresia: Double,
		promocion: Promotion?,
		tipoPago: String
	): Double {
		if (tipoPago != "Dólares" || promocion == null) return precioMembresia
		val descuento = promocion.porcentajeDescuento
		val montoFinal = precioMembresia - (precioMembresia * descuento / 100)
		return String.format("%.2f", montoFinal).toDouble()
	}
	
	fun clearError() {
		errorMessage = null
	}
	
	fun clearPromotionMessage() {
		_promotionActionMessage.value = null
		_isActionSuccess.value = null
	}
}