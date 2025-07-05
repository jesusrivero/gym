package com.jesus.gymcontrol.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.model.PaymentState
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.usecase.usuario.AddPaymentUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.getDates.GetAllPaymentsUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.payment.CalcularNuevaFechaVencimientoUseCase
import com.jesus.gymcontrol.domain.usecase.usuario.payment.GenerarDescripcionPagoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
	private val addPaymentUseCase: AddPaymentUseCase,
	private val getAllPaymentsUseCase: GetAllPaymentsUseCase,
	private val sessionManager: SessionManager,
	private val calcularNuevaFechaVencimientoUseCase: CalcularNuevaFechaVencimientoUseCase,
	private val generarDescripcionPagoUseCase: GenerarDescripcionPagoUseCase,
	
	private val firestore: FirebaseFirestore,
) : ViewModel() {
	
	private val _paymentState = MutableStateFlow(PaymentState())
	val paymentState: StateFlow<PaymentState> = _paymentState
	
	var payments by mutableStateOf<List<Payment>>(emptyList())
		private set
	
	
	var selectedPromotion by mutableStateOf<Promotion?>(null)
		private set
	
	var isLoading by mutableStateOf(false)
		private set
	
	var isSuccess by mutableStateOf(false)
		private set
	
	var errorMessage by mutableStateOf<String?>(null)
		private set
	
	var descripcionGenerada by mutableStateOf<String?>(null)
		internal set
	
	val paymentActionMessage = mutableStateOf<String?>(null)
	val isActionSuccess = mutableStateOf<Boolean?>(null)
	
	
	fun selectedPromotion(promotion: Promotion?) {
		selectedPromotion = promotion
	}
	
	/** Carga el mapa completo de membresías y precios */
	fun setMemberships(memberships: List<Membership>) {
		val membershipMap = memberships.associate { it.nombre to it.precio }
		_paymentState.value = _paymentState.value.copy(listMembership = membershipMap)
	}
	
	/** Solo actualiza la frecuencia seleccionada, sin tocar el mapa */
	fun updatePaymentFrequency(frequency: String) {
		_paymentState.update {
			it.copy(frequency = frequency)
		}
	}
	
	/** Actualiza el tipo de pago */
	fun updatePaymentType(type: String) {
		_paymentState.update {
			it.copy(type = type)
		}
	}
	
	/** Actualiza el monto en dólares */
	fun updateAmountDollar(amount: String) {
		_paymentState.update {
			it.copy(amountDollar = amount)
		}
	}
	
	/** Actualiza el monto en bolívares */
	fun updateAmountBs(amount: String) {
		_paymentState.update {
			it.copy(amountBs = amount)
		}
	}
	
	/** Agrega un pago usando el caso de uso */
	fun addPago(pago: Pago) {
		viewModelScope.launch {
			isLoading = true
			isSuccess = false
			errorMessage = null
			
			val result = addPaymentUseCase(pago)
			
			result.onSuccess {
				isSuccess = true
			}.onFailure {
				errorMessage = it.message
			}
			
			isLoading = false
		}
	}
	
	fun loadPayments() {
		val gymCode = sessionManager.getGymCode() ?: return
		viewModelScope.launch {
			isLoading = true
			try {
				payments = getAllPaymentsUseCase(gymCode)
			} catch (e: Exception) {
				Log.e("PaymentsViewModel", "Error loading payments", e)
			} finally {
				isLoading = false
			}
		}
	}
	
	fun calculateDiscountedAmountIfApplicable() {
		val type = _paymentState.value.type
		val frequency = _paymentState.value.frequency
		val membershipPrice = _paymentState.value.listMembership[frequency] ?: return
		
		if (type != "Dólares") {
			selectedPromotion = null
			_paymentState.update {
				it.copy(amountDollar = membershipPrice.toString())
			}
			return
		}
		
		val discount = selectedPromotion?.porcentajeDescuento ?: 0.0
		val discountedPrice = membershipPrice - (membershipPrice * (discount / 100.0))
		
		_paymentState.update {
			it.copy(amountDollar = String.format(Locale.US, "%.2f", discountedPrice))
		}
	}
	
	/** Limpia estados de éxito y error */
	fun resetState() {
		isSuccess = false
		errorMessage = null
		_paymentState.update {
			it.copy(
				type = "",
				frequency = "",
				amountDollar = "",
				amountBs = ""
			)
		}
	}
	
	
	fun registrarPago(pago: Pago, membershipDays: Int) {
		viewModelScope.launch {
			isLoading = true
			isSuccess = false
			errorMessage = null
			
			try {
				val nuevaFechaVencimiento = calcularNuevaFechaVencimientoUseCase(
					pago.userId,
					pago.gimnasioCode,
					membershipDays
				)
				
				val pagoConFecha = pago.copy(fechaVencimiento = nuevaFechaVencimiento)
				
				val result = addPaymentUseCase(pagoConFecha)
				
				result.onSuccess {
					// 👉 ACTUALIZA la fechaVencimiento también en el documento del usuario
					firestore.collection("users")
						.document(pago.userId)
						.collection("gimnasios")
						.document(pago.gimnasioCode)
						.update("fechaVencimiento", nuevaFechaVencimiento)
					
					result.onSuccess {
						firestore.collection("users")
							.document(pago.userId)
							.collection("gimnasios")
							.document(pago.gimnasioCode)
							.update("fechaVencimiento", nuevaFechaVencimiento)
						
						paymentActionMessage.value = "Pago registrado correctamente, deseas registrar otro?"
						isActionSuccess.value = true
					}.onFailure {
						paymentActionMessage.value = "Error al registrar el pago: ${it.message}"
						isActionSuccess.value = false
					}
				}.onFailure {
					errorMessage = it.message
				}
			} catch (e: Exception) {
				paymentActionMessage.value = "Error al registrar el pago: ${e.message}"
				isActionSuccess.value = false
			} finally {
				isLoading = false
			}
		}
	}
	
	
	fun generarDescripcion(userId: String, gymCode: String, nuevaMembresia: String) {
		viewModelScope.launch {
			descripcionGenerada = try {
				generarDescripcionPagoUseCase(userId, gymCode, nuevaMembresia)
			} catch (e: Exception) {
				null
			}
		}
	}
	

	
	fun clearPaymentAction() {
		paymentActionMessage.value = null
		isActionSuccess.value = null
	}
	
	fun clearDescription() {
		descripcionGenerada = null
	}
}