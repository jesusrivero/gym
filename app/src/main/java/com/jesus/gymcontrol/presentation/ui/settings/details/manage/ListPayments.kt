package com.jesus.gymcontrol.presentation.ui.settings.details.manage

import android.os.Build
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
	
	LaunchedEffect(Unit) {
		viewModel.loadPayments()
	}
	
	if (showDialog && selectedPayment != null) {
		PaymentDetailDialog(payment = selectedPayment!!, onDismiss = {
			showDialog = false
			selectedPayment = null
		})
	}
	
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
		val filteredList = payments.filter {
			(selectedPaymentType == "Todos" || it.paymentType.equals(selectedPaymentType, true)) &&
					(searchText.isBlank()
							|| it.name.contains(searchText, true)
							|| it.reference?.contains(searchText, true) == true
							|| it.membershipName.contains(searchText, true))
		}
		
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			when {
				isLoading -> {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
						Spacer(modifier = Modifier.height(12.dp))
						Text("Cargando pagos...", color = MaterialTheme.colorScheme.onSurfaceVariant)
					}
				}
				
				filteredList.isEmpty() -> {
					Text(
						text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay pagos registrados",
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				
				else -> {
					LazyColumn(
						modifier = Modifier
							.fillMaxSize()
							.padding(horizontal = 16.dp),
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
		}
	}
}

@Composable
fun PaymentCard(payment: Payment, onViewDetails: () -> Unit) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(2.dp),
		colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = payment.name,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = payment.membershipName,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Spacer(modifier = Modifier.height(8.dp))
				Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
					PaymentInfoBadge("Monto", "${payment.amount} $")
					PaymentInfoBadge("Tipo", payment.paymentType)
				}
			}
			IconButton(onClick = onViewDetails) {
				Icon(
					painter = painterResource(id = R.drawable.ic_details),
					contentDescription = "Detalles",
					tint = MaterialTheme.colorScheme.primary
				)
			}
		}
	}
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
fun PaymentDetailDialog(payment: Payment, onDismiss: () -> Unit) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(
				text = "Detalles del pago",
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center
			)
		},
		text = {
			Column {
				DetailRow("Nombre:", payment.name)
				Spacer(modifier = Modifier.height(8.dp))
				DetailRow("Membresía:", payment.membershipName)
				Spacer(modifier = Modifier.height(8.dp))
				DetailRow("Tipo de pago:", payment.paymentType)
				Spacer(modifier = Modifier.height(8.dp))
				DetailRow("Monto:", "${payment.amount} $")
				payment.reference?.let {
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Referencia:", it)
				}
			}
		},
		confirmButton = {
			Button(
				onClick = onDismiss,
				modifier = Modifier.fillMaxWidth(),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary
				)
			) {
				Text("Cerrar")
			}
		}
	)
}


