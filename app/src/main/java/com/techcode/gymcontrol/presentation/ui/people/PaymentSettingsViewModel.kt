package com.techcode.gymcontrol.presentation.ui.people

import androidx.lifecycle.ViewModel
import com.techcode.gymcontrol.data.sharedPreferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentSettingsViewModel @Inject constructor(
	private val PreferencesManager: PreferencesManager,
) : ViewModel() {
	
	
	private val _paymentValues = MutableStateFlow(
		mapOf(
			"Semanal" to "4",
			"Quincenal" to "8",
			"Mensual" to "15",
			"Trimestral" to "40",
			"Semestral" to "85",
			"Anual" to "160"
		)
	)
	val paymentValues: StateFlow<Map<String, String>> = _paymentValues
	
	
	private val _paymentState = MutableStateFlow(PaymentState())
	val paymentState: StateFlow<PaymentState> = _paymentState
	
	data class PaymentState(
		val frequency: String = "",
		val type: String = "",
		val amountDollar: String = "",
		val amountBs: String = "",
	)
	
	fun updatePaymentValues(newValues: Map<String, String>) {
		_paymentValues.value = newValues
	}
	
	fun updatePaymentFrequency(frequency: String) {
		_paymentState.value = _paymentState.value.copy(frequency = frequency)
		updateAmounts()
	}
	
	fun updatePaymentType(type: String) {
		_paymentState.value = _paymentState.value.copy(type = type)
		updateAmounts()
	}
	
	fun updateAmountDollar(amount: String) {
		_paymentState.value = _paymentState.value.copy(amountDollar = amount)
	}
	
	fun updateAmountBs(amount: String) {
		_paymentState.value = _paymentState.value.copy(amountBs = amount)
	}
	
	private fun updateAmounts() {
		val current = _paymentState.value
		if (current.type == "Dólares" && current.frequency.isNotEmpty()) {
			_paymentState.value = current.copy(
				amountDollar = _paymentValues.value[current.frequency] ?: "",
				amountBs = ""
			)
		} else if (current.type == "Bolívares") {
			_paymentState.value = current.copy(amountDollar = "")
		}
	}
}