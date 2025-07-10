package com.jesus.gymcontrol.presentation.ui.settings.details.manage

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.generateInvoicePdf
import com.jesus.gymcontrol.domain.model.Payment
import com.jesus.gymcontrol.domain.viewmodels.PaymentsViewModel
import com.jesus.gymcontrol.presentation.ui.commons.PaymentFilters
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPaymentsScreen(
	navBottom: NavController,
	viewModel: PaymentsViewModel = hiltViewModel(),
	navPagToScreen: (String, String) -> Unit,
) {
	val payments = viewModel.payments
	val isLoading = viewModel.isLoading
	var showDialog by rememberSaveable { mutableStateOf(false) }
	var selectedPayment by rememberSaveable { mutableStateOf<Payment?>(null) }
	var searchText by rememberSaveable { mutableStateOf("") }
	var selectedPaymentType by rememberSaveable { mutableStateOf("Todos") }
	val paymentTypeOptions = listOf("Todos", "Dólares", "Bolívares", "Mixto", "Promociones")
	val configuration = LocalConfiguration.current
	val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
	
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
		}
	) { innerPadding ->
		val filteredList = payments.filter { payment ->
			val matchesSearch = searchText.isBlank() ||
					payment.name.contains(searchText, ignoreCase = true) ||
					payment.reference?.contains(searchText, ignoreCase = true) == true ||
					payment.membershipName.contains(searchText, ignoreCase = true)
			
			val matchesFilter = when (selectedPaymentType) {
				"Todos" -> true
				"Dólares", "Bolívares", "Mixto" -> payment.paymentType.equals(
					selectedPaymentType,
					ignoreCase = true
				)
				
				"Promociones" -> !payment.promocionNombre.isNullOrBlank()
				else -> true
			}
			
			matchesSearch && matchesFilter
		}
		
		if (isLandscape) {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.padding(horizontal = 16.dp),
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				item {
					PaymentFilters(
						selectedPaymentType = selectedPaymentType,
						paymentTypeOptions = paymentTypeOptions,
						onPaymentTypeSelected = { selectedPaymentType = it },
						onClearFilters = {
							selectedPaymentType = "Todos"
							searchText = ""
						},
						searchText = searchText,
						onSearchTextChanged = { searchText = it },
						onAddClick = {
							// 👇 Aquí pasamos valores predeterminados
							navPagToScreen("nuevoUid", "Nuevo cliente")
						},
						showAddButton = true
					)
				}
				
				when {
					isLoading -> {
						item {
							Column(horizontalAlignment = Alignment.CenterHorizontally) {
								CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
								Spacer(modifier = Modifier.height(12.dp))
								Text("Cargando pagos...", color = MaterialTheme.colorScheme.onSurfaceVariant)
							}
						}
					}
					
					filteredList.isEmpty() -> {
						item {
							Text(
								text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay pagos registrados",
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					}
					
					else -> {
						items(filteredList) { payment ->
							PaymentCard(payment) {
								selectedPayment = payment
								showDialog = true
							}
						}
					}
				}
			}
		} else {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
			) {
				PaymentFilters(
					selectedPaymentType = selectedPaymentType,
					paymentTypeOptions = paymentTypeOptions,
					onPaymentTypeSelected = { selectedPaymentType = it },
					onClearFilters = {
						selectedPaymentType = "Todos"
						searchText = ""
					},
					searchText = searchText,
					onSearchTextChanged = { searchText = it },
					onAddClick = {
						// 👇 Aquí también pasamos valores predeterminados
						navPagToScreen("nuevoUid", "Nuevo cliente")
					},
					showAddButton = true
				)
				
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 16.dp),
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
									.fillMaxSize(),
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
	}
}


@Composable
fun PaymentCard(payment: Payment, onViewDetails: () -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight()
			.padding(horizontal = 2.dp),
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(1.dp),
		colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 12.dp, vertical = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			// Nombre
			Text(
				text = payment.name,
				style = MaterialTheme.typography.bodyMedium,
				fontWeight = FontWeight.SemiBold,
				modifier = Modifier.weight(1.2f)
			)
			
			// Membresía
			Text(
				text = payment.membershipName,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.weight(1f)
			)
			
			// Tipo
			Text(
				text = payment.paymentType,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.weight(1f),
				textAlign = TextAlign.End
			)
			
			// Precio
			val amountText = when (payment.paymentType.lowercase()) {
				"dólares" -> "${payment.amountDollar} $"
				"mixto" -> "${payment.amount} $"
				"bolívares" -> "${payment.amountBs} Bs"
				else -> "-"
			}
			
			Text(
				text = amountText,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.weight(1f),
				textAlign = TextAlign.End
			)
			
			// Botón de detalles
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

//@Composable
//fun PaymentInfoBadge(label: String, value: String) {
//	Surface(
//		shape = RoundedCornerShape(8.dp),
//		color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//		contentColor = MaterialTheme.colorScheme.primary
//	) {
//		Column(
//			modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
//			horizontalAlignment = Alignment.CenterHorizontally
//		) {
//			Text(text = label, style = MaterialTheme.typography.labelSmall)
//			Text(
//				text = value,
//				style = MaterialTheme.typography.bodySmall,
//				fontWeight = FontWeight.Bold
//			)
//		}
//	}
//}


@Composable
fun formatAmount(value: Double): String {
	val locale = Locale("es", "VE")
	val formatter = NumberFormat.getNumberInstance(locale).apply {
		minimumFractionDigits = 2
		maximumFractionDigits = 2
	}
	return formatter.format(value)
}

@Composable
fun PaymentDetailDialog(
	payment: Payment,
	onDismiss: () -> Unit,
) {
	val context = LocalContext.current
	
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(
				text = "Detalles del pago",
				modifier = Modifier.fillMaxWidth(),
				textAlign = TextAlign.Center,
				style = MaterialTheme.typography.titleLarge
			)
		},
		text = {
			Column(modifier = Modifier.fillMaxWidth()) {
				DetailRow("Nombre:", payment.name)
				Spacer(modifier = Modifier.height(8.dp))
				
				DetailRow("Membresía:", payment.membershipName)
				Spacer(modifier = Modifier.height(8.dp))
				
				DetailRow("Tipo de pago:", payment.paymentType)
				Spacer(modifier = Modifier.height(8.dp))
				
				DetailRow("Monto total:", "$${formatAmount(payment.amount)}")
				
				when (payment.paymentType) {
					"Mixto" -> {
						payment.amountDollar.takeIf { it != null && it > 0 }?.let {
							DetailRow("Pagado en dólares:", "$${formatAmount(it)}")
						}
						payment.amountBs.takeIf { it != null && it > 0 }?.let {
							DetailRow("Pagado en bolívares:", "Bs. ${formatAmount(it)}")
						}
					}
					
					"Dólares" -> {
						DetailRow("Pagado en dólares:", "$${formatAmount(payment.amountDollar)}")
					}
					
					"Bolívares" -> {
						DetailRow("Pagado en bolívares:", "Bs. ${formatAmount(payment.amountBs)}")
					}
				}
				
				payment.reference?.let {
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Referencia:", it)
				}
				
				if (!payment.promocionNombre.isNullOrBlank()) {
					Spacer(modifier = Modifier.height(12.dp))
					DetailRow("Promoción:", payment.promocionNombre ?: "")
				}
				if (payment.promocionPorcentajeDescuento != null) {
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Descuento aplicado:", "${payment.promocionPorcentajeDescuento}%")
				}
				
				Spacer(modifier = Modifier.height(8.dp))
				val formattedDate =
					SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.date))
				DetailRow("Fecha de pago:", formattedDate)
				
				Spacer(modifier = Modifier.height(8.dp))
				val formattedVencimiento =
					SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.fechaVencimiento))
				DetailRow("Fecha de vencimiento:", formattedVencimiento)
				
				Spacer(modifier = Modifier.height(8.dp))
				
				Text(
					text = "Descripción:",
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.Bold
				)
				Spacer(modifier = Modifier.height(4.dp))
				
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.heightIn(max = 150.dp)
						.verticalScroll(rememberScrollState())
						.padding(4.dp)
				) {
					Text(
						text = payment.description,
						style = MaterialTheme.typography.bodyMedium,
						softWrap = true
					)
				}
			}
		},
		containerColor = MaterialTheme.colorScheme.surface,
		confirmButton = {
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Button(
					onClick = {
						val pdfFile = generateInvoicePdf(context, payment)
						shareFile(context, pdfFile, "application/pdf")
					},
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primary,
						contentColor = MaterialTheme.colorScheme.onPrimary
					)
				) {
					Icon(
						imageVector = Icons.Default.Share,
						contentDescription = "Compartir",
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text("Compartir factura (PDF)")
				}
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
		}
	)
}


fun shareFile(context: Context, file: File, mimeType: String) {
	val uri = FileProvider.getUriForFile(
		context,
		"${context.packageName}.fileprovider",
		file
	)
	
	val intent = Intent(Intent.ACTION_SEND).apply {
		type = mimeType
		putExtra(Intent.EXTRA_STREAM, uri)
		addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	}
	
	context.startActivity(
		Intent.createChooser(intent, "Compartir factura")
	)
}


//@Composable
//fun PaymentInvoiceView(payment: Payment) {
//	Column(
//		modifier = Modifier
//			.background(Color.White)
//			.padding(16.dp)
//			.fillMaxWidth()
//	) {
//		Text("Factura de Pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//
//		DetailRow("Nombre:", payment.name)
//		Spacer(modifier = Modifier.height(8.dp))
//
//		DetailRow("Membresía:", payment.membershipName)
//		Spacer(modifier = Modifier.height(8.dp))
//
//		DetailRow("Tipo de pago:", payment.paymentType)
//		Spacer(modifier = Modifier.height(8.dp))
//
//		when (payment.paymentType) {
//			"Mixto" -> {
//				DetailRow("Monto total:", formatMonto(payment))
//				payment.amountDollar.takeIf { it > 0 }?.let {
//					DetailRow("Dólares:", "$${formatAmount(it)}")
//				}
//				payment.amountBs.takeIf { it > 0 }?.let {
//					DetailRow("Bolívares:", "Bs. ${formatAmount(it)}")
//				}
//			}
//			"Dólares" -> {
//				DetailRow("Monto:", "$${formatAmount(payment.amountDollar)}")
//			}
//			"Bolívares" -> {
//				DetailRow("Monto:", "Bs. ${formatAmount(payment.amountBs)}")
//			}
//		}
//
//		payment.reference?.let {
//			Spacer(modifier = Modifier.height(8.dp))
//			DetailRow("Referencia:", it)
//		}
//
//		if (!payment.promocionNombre.isNullOrBlank()) {
//			Spacer(modifier = Modifier.height(12.dp))
//			DetailRow("Promoción:", payment.promocionNombre ?: "")
//		}
//		if (payment.promocionPorcentajeDescuento != null) {
//			Spacer(modifier = Modifier.height(8.dp))
//			DetailRow("Descuento aplicado:", "${payment.promocionPorcentajeDescuento}%")
//		}
//
//		Spacer(modifier = Modifier.height(8.dp))
//		val formattedDate =
//			SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.date))
//		DetailRow("Fecha de pago:", formattedDate)
//
//		Spacer(modifier = Modifier.height(8.dp))
//		val formattedVencimiento =
//			SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(payment.fechaVencimiento))
//		DetailRow("Fecha de vencimiento:", formattedVencimiento)
//
//		Spacer(modifier = Modifier.height(8.dp))
//
//		// 📝 Descripción adaptativa
//		Text(
//			text = "Descripción:",
//			style = MaterialTheme.typography.bodyMedium,
//			fontWeight = FontWeight.Bold
//		)
//		Spacer(modifier = Modifier.height(4.dp))
//
//		Box(
//			modifier = Modifier
//				.fillMaxWidth()
//				.heightIn(max = 150.dp) // máximo 150dp alto
//				.verticalScroll(rememberScrollState())
//				.padding(4.dp)
//		) {
//			Text(
//				text = payment.description,
//				style = MaterialTheme.typography.bodyMedium,
//				softWrap = true
//			)
//		}
//	}
//}
