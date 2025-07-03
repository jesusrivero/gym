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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionViewModel @Inject constructor(
	private val createPromotionUseCase: CreatePromotionUseCase,
	private val getPromotionsUseCase: GetPromotionUseCase,
	private val updatePromotionUseCase: UpdatePromotionUseCase,
	private val deletePromotionUseCase: DeletePromotionUseCase,
	private val getUsersCountByPromotionUseCase:GetUsersCountByPromotionUseCase,
	private val togglePromotionStateUseCase: UpdatePromotionUseCase,
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
	
	fun createPromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			errorMessage = null
			
			val result = createPromotionUseCase(promotion)
			isLoading = false
			result.onSuccess{
				loadPromotions()
			}
			result.onFailure {
				errorMessage = it.message
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
				loadPromotions()
			}.onFailure {
				errorMessage = it.message
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
	
	fun updatePromotion(promotion: Promotion) {
		viewModelScope.launch {
			_promotionActionMessage
			isLoading = true
			errorMessage = null
			
			val result = updatePromotionUseCase(promotion)
			isLoading = false
			result.onSuccess{
				loadPromotions()
			}
			result.onFailure {
				errorMessage = it.message
			}
			
			loadPromotions()
			}
		}
	
	fun deletePromotion(promotion: Promotion) {
		viewModelScope.launch {
			isLoading = true
			val result = deletePromotionUseCase(promotion)
			isLoading = false
			result.onSuccess{
				loadPromotions()
			}
			result.onFailure { errorMessage = it.message }
			loadPromotions()
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
	
	fun clearpromotionMessage() {
		_promotionActionMessage.value = null
	}
	
}