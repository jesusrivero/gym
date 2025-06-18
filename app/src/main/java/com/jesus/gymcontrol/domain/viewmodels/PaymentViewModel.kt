package com.jesus.gymcontrol.domain.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.usecase.usuario.AddPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val addPaymentUseCase: AddPaymentUseCase,
) : ViewModel() {

    data class PaymentState(
        val frequency: String = "",
        val type: String = "",
        val amountDollar: String = "",
        val amountBs: String = "",
        val listMembership: Map<String, Double> = emptyMap()  // aquí minúscula inicial
    )

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState: StateFlow<PaymentState> = _paymentState

    var isLoading by mutableStateOf(false)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

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

    /** Limpia estados de éxito y error */
    fun resetState() {
        isSuccess = false
        errorMessage = null
    }
}