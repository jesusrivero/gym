package com.jesus.gymcontrol.presentation.ui.settings.details.manage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.viewmodels.PaymentsViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.PaymentFilters


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPaymentsScreen(
	navBottom: NavController,
	viewModel: PaymentsViewModel = hiltViewModel(),
	navPagToScreen: () -> Unit
) {
	val payments = viewModel.payments
	val isLoading = viewModel.isLoading
	
	var showDialog by remember { mutableStateOf(false) }
	var selectedPayment by remember { mutableStateOf<Payment?>(null) }
	var searchText by remember { mutableStateOf("") }
	var selectedPaymentType by remember { mutableStateOf("Todos") }
	
	val paymentTypeOptions = listOf("Todos", "Dólares", "Bolívares", "Mixto")
	
	LaunchedEffect(viewModel.payments) {
		Log.d("ListPaymentsScreen", "Payments: ${viewModel.payments}")
	}
	
	LaunchedEffect(Unit) {
		viewModel.loadPayments()
	}
	
	GymTheme {
		Scaffold(
			topBar = {
				Column {
					TopAppBar(
						title = {
							Text(
								text = "Listado de pagos",
								color = MaterialTheme.colorScheme.onPrimary,
								fontWeight = FontWeight.Bold
							)
						},
						navigationIcon = {
							IconButton(onClick = { navBottom.popBackStack() }) {
								Icon(
									painter = painterResource(id = R.drawable.ic_back),
									contentDescription = "Regresar",
									tint = MaterialTheme.colorScheme.onPrimary
								)
							}
						},
						colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
							containerColor = MaterialTheme.colorScheme.primary
						)
					)
					
					PaymentFilters(
						selectedPaymentType = selectedPaymentType,
						paymentTypeOptions = paymentTypeOptions,
						onPaymentTypeSelected = { selectedPaymentType = it },
						onClearFilters = {
							selectedPaymentType = "Todos"
							searchText = ""
						},
						searchText = searchText,
						onSearchTextChanged = { searchText = it }
					)
				}
			},
			floatingActionButton = {
				FloatingActionButton(
					onClick = navPagToScreen,
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary
				) {
					Icon(Icons.Default.Add, contentDescription = "Nuevo pago")
				}
			}
		) { innerPadding ->
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding),
				contentAlignment = Alignment.Center
			) {
				if (isLoading) {
					CircularProgressIndicator()
				} else {
					val filteredList = payments.filter {
						(selectedPaymentType == "Todos" || it.typePayment?.equals(selectedPaymentType, true) == true) &&
								(searchText.isBlank()
										|| it.name.contains(searchText, true)
										|| it.idCard.contains(searchText, true)
										|| it.reference?.contains(searchText, true) == true)
					}
					
					if (filteredList.isEmpty()) {
						Text(
							text = "No hay pagos registrados",
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)
					} else {
						LazyColumn(
							modifier = Modifier
								.fillMaxSize()
								.padding(16.dp),
							verticalArrangement = Arrangement.spacedBy(12.dp)
						) {
							items(filteredList) { payment ->
								PaymentCard(payment) {
									selectedPayment = payment
									showDialog = true
								}
							}
						}
					}
				}
				
				if (showDialog && selectedPayment != null) {
					PaymentDetailDialog(
						payment = selectedPayment!!,
						onDismiss = { showDialog = false }
					)
				}
			}
		}
	}
}

@Composable
fun PaymentCard(payment: Payment, onViewDetails: () -> Unit) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(16.dp)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = payment.name,
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)
					Text(
						text = payment.idCard,
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
					Spacer(modifier = Modifier.height(8.dp))
					Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
						PaymentInfoBadge("Monto", "${payment.amount} $")
//						PaymentInfoBadge("Fecha", payment.date.formatAsDate())
					}
				}
				IconButton(onClick = onViewDetails) {
					Icon(
						painter = painterResource(id = R.drawable.ic_details),
						contentDescription = "Detalles"
					)
				}
			}
		}
	}
}

@Composable
fun PaymentDetailDialog(payment: Payment, onDismiss: () -> Unit) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(
				"Detalles del pago",
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center
			)
		},
		text = {
			Column {
				DetailRow("Nombre:", payment.name)
				DetailRow("Cédula:", payment.idCard)
				DetailRow("Membresía:", payment.membership)
				DetailRow("Tipo de pago:", payment.typePayment)
				DetailRow("Monto:", "${payment.amount} $")
				payment.reference?.let {
					DetailRow("Referencia:", it)
				}
				if (!payment.description.isNullOrBlank()) {
					DetailRow("Descripción:", payment.description)
				}
//				DetailRow("Fecha:", payment.date.formatAsDate())
			}
		},
		confirmButton = {
			Button(
				onClick = onDismiss,
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Cerrar")
			}
		}
	)
}

@Composable
fun PaymentInfoBadge(label: String, value: String) {
	Surface(
		shape = RoundedCornerShape(8.dp),
		color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
		contentColor = MaterialTheme.colorScheme.primary
	) {
		Column(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(text = label, style = MaterialTheme.typography.labelSmall)
			Text(
				text = value,
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Bold
			)
		}
	}
}

@Composable
fun DetailRowDetailRow(label: String, value: String) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(label, fontWeight = FontWeight.Bold)
		Text(value)
	}
}

