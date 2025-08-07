package com.jesus.gymcontrol.domain.helpers

sealed class PaymentUiState {
	object Loading : PaymentUiState()
	object Success : PaymentUiState()
	data class Error(val message: String) : PaymentUiState()
	object NoInternet : PaymentUiState()
}