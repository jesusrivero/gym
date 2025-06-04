package com.jesus.gymcontrol.presentation.ui.people

import androidx.lifecycle.ViewModel
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import com.jesus.gymcontrol.domain.model.PricesMembership
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
        val MonthlyValue: Double = 0.0,
        val WeeklyValue: Double = 0.0,
        val BiweeklyValue: Double = 0.0,
        val QuarterlyValue: Double = 0.0,
        val BiannualValue: Double = 0.0,
        val AnnualValue: Double = 0.0,
        val ListMembership: Map<String, Double> = emptyMap(),
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

        _paymentState.value = _paymentState.value.copy(
            WeeklyValue = WeeklyValue?.toDoubleOrNull() ?: 0.0,
            BiweeklyValue = BiweeklyValue?.toDoubleOrNull() ?: 0.0,
            MonthlyValue = MonthlyValue?.toDoubleOrNull() ?: 0.0,
            QuarterlyValue = QuarterlyValue?.toDoubleOrNull() ?: 0.0,
            BiannualValue = BiannualValue?.toDoubleOrNull() ?: 0.0,
            AnnualValue = AnnualValue?.toDoubleOrNull() ?: 0.0,
            ListMembership = mapOf(
                "Semanal" to (WeeklyValue?.toDoubleOrNull() ?: 0.0),
                "Quincenal" to (BiweeklyValue?.toDoubleOrNull() ?: 0.0),
                "Mensual" to (MonthlyValue?.toDoubleOrNull() ?: 0.0),
                "Trimestral" to (QuarterlyValue?.toDoubleOrNull() ?: 0.0),
                "Semestral" to (BiannualValue?.toDoubleOrNull() ?: 0.0),
                "Anual" to (AnnualValue?.toDoubleOrNull() ?: 0.0)
            )
        )

    }

    fun getPricesValue() {
        val prices = PreferencesManager.getPrices()
        val weeklyValue = prices?.weekly?.toDoubleOrNull() ?: 0.0
        val biweeklyValue = prices?.biweekly?.toDoubleOrNull() ?: 0.0
        val monthlyValue = prices?.monthly?.toDoubleOrNull() ?: 0.0
        val quarterlyValue = prices?.quarterly?.toDoubleOrNull() ?: 0.0
        val biannualValue = prices?.biannual?.toDoubleOrNull() ?: 0.0
        val annualValue = prices?.annual?.toDoubleOrNull() ?: 0.0

        _paymentState.value = _paymentState.value.copy(
            WeeklyValue = weeklyValue,
            BiweeklyValue = biweeklyValue,
            MonthlyValue = monthlyValue,
            QuarterlyValue = quarterlyValue,
            BiannualValue = biannualValue,
            AnnualValue = annualValue,
            PricesMembership = prices,
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

//    fun updateBiweeklyValue(newValue: Int) {
//        PreferencesManager.saveData(KEY_BIWEEKLY_VALUE, newValue)
//    }
//
//    fun getBiweeklyValue() {
//        val biweeklyValue = PreferencesManager.getData(KEY_BIWEEKLY_VALUE)
//        _paymentState.value = _paymentState.value.copy(BiweeklyValue = biweeklyValue)
//    }


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
                amountDollar = _paymentState.value.ListMembership[current.frequency].toString()
                    ?: "",
                amountBs = ""
            )
        } else if (current.type == "Bolívares") {
            _paymentState.value = current.copy(amountDollar = "")
        }
    }
}