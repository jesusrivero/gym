package com.techcode.gymcontrol.presentation.ui.people

import androidx.lifecycle.ViewModel
import com.techcode.gymcontrol.data.sharedPreferences.PreferencesManager
import com.techcode.gymcontrol.data.sharedPreferences.PreferencesManager.Companion.KEY_BIWEEKLY_VALUE
import com.techcode.gymcontrol.domain.model.PricesMembership
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
		val MonthlyValue: Int = 0,
		val WeeklyValue: Int = 0,
		val BiweeklyValue: Int = 0,
		val QuarterlyValue: Int = 0,
		val BinnualValue: Int = 0,
		val AnnualValue: Int = 0,
		val ListMembership: Map<String, Int> = emptyMap(),
		val PricesMembership: PricesMembership? = null,
	)
	
	
	
	fun updatePricesMembership(
		MonthlyValue: String?,
		BiweeklyValue: String?,
		WeeklyValue: String?,
		QuarterlyValue: String?,
		BiannualValue: String?,
		AnnualValue: String?,
	) {
		PreferencesManager.savePrices(
			PricesMembership(
				weekly = WeeklyValue,
				biweekly = BiweeklyValue,
				monthly = MonthlyValue,
				quarterly = QuarterlyValue,
				biannual = BiannualValue,
				annual = AnnualValue
			)
		)
		
	}
	
	fun getPricesValue() {
		val prueba = PreferencesManager.getPrices()
		val weeklyValue = prueba?.weekly?.toInt() ?: 0
		val biweeklyValue = prueba?.biweekly?.toInt() ?: 0
		val monthlyValue = prueba?.monthly?.toInt() ?: 0
		val quarterlyValue = prueba?.quarterly?.toInt() ?: 0
		val biannualValue = prueba?.biannual?.toInt() ?: 0
		val annualValue = prueba?.annual?.toInt() ?: 0
		
		
		_paymentState.value = _paymentState.value.copy(
			ListMembership = mapOf(
				"Semanal" to weeklyValue,
				"Quincenal" to biweeklyValue,
				"Mensual" to monthlyValue,
				"Trimestral" to quarterlyValue,
				"Semestral" to biannualValue,
				"Anual" to annualValue
			
			)
		)
		
	}
	
	fun updateBiweeklyValue(newValue: Int) {
		PreferencesManager.saveData(KEY_BIWEEKLY_VALUE, newValue)
	}
	
	fun getBiweeklyValue() {
		val biweeklyValue = PreferencesManager.getData(KEY_BIWEEKLY_VALUE)
		_paymentState.value = _paymentState.value.copy(BiweeklyValue = biweeklyValue)
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

	fun resetPaymentState() {
		_paymentState.value = _paymentState.value.copy(
			frequency = "",
			type = "",
			amountDollar = "",
			amountBs = ""
		)
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