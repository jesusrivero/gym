package com.techcode.gymcontrol.presentation.ui.people

import androidx.lifecycle.ViewModel
import com.techcode.gymcontrol.data.sharedPreferences.PreferencesManager
import com.techcode.gymcontrol.data.sharedPreferences.PreferencesManager.Companion.KEY_WEEKLY_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentSettingsViewModel @Inject constructor(
	private val PreferencesManager: PreferencesManager,
) : ViewModel() {
	
	
	private val _paymentState = MutableStateFlow(PaymentState())
	val paymentState: StateFlow<PaymentState> = _paymentState
	
	data class PaymentState(
		val frequency: String = "",
		val type: String = "",
		val amountDollar: String = "",
		val amountBs: String = "",
		val WeeklyValue: Int = 0,
		val ListMembership: Map<String, Int> = emptyMap(),
	)
	
	init {
		getWeeklyValue()
		
		_paymentState.value = _paymentState.value.copy(
			ListMembership = mapOf(
				"Semanal" to _paymentState.value.WeeklyValue,
				"Quincenal" to 8,
				"Mensual" to 15,
				"Trimestral" to 40,
				"Semestral" to 85,
				"Anual" to 160
			
			)
		)
	}
	
	fun updateWeeklyValue(newValue: Int) {
		PreferencesManager.saveData(KEY_WEEKLY_VALUE, newValue)
	}
	
	fun getWeeklyValue() {
		val weeklyValue = PreferencesManager.getData(KEY_WEEKLY_VALUE)
		_paymentState.value = _paymentState.value.copy(WeeklyValue = weeklyValue)
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
				amountDollar = _paymentState.value.ListMembership[current.frequency].toString() ?: "",
				amountBs = ""
			)
		} else if (current.type == "Bolívares") {
			_paymentState.value = current.copy(amountDollar = "")
		}
	}
}