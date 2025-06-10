package com.jesus.gymcontrol.presentation.ui.settings.details.payments


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.ui.people.PaymentSettingsViewModel
import kotlinx.coroutines.delay


@Composable
fun PaymentsScreen(navController: NavController) {
	PaymentsScreenContent(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreenContent(navController: NavController) {
	val colorScheme = MaterialTheme.colorScheme
	val viewModel: PaymentSettingsViewModel = hiltViewModel()
	val paymentState by viewModel.paymentState.collectAsState()
	
	var description by remember { mutableStateOf("") }
	var nameUser by remember { mutableStateOf("") }
	var reference by remember { mutableStateOf("") }
	var isDropdownExpanded by remember { mutableStateOf(false) }
	var isTypeDropdownExpanded by remember { mutableStateOf(false) }
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	
	val paymentTypes = listOf("Dólares", "Bolívares", "Mixto")
	val isPaymentTypeEnabled = paymentState.frequency.isNotEmpty()
	
	val formIsValid = nameUser.isNotBlank() &&
			paymentState.frequency.isNotBlank() &&
			paymentState.type.isNotBlank() &&
			when (paymentState.type) {
				"Dólares" -> paymentState.amountDollar.toFloatOrNull()?.let { it > 0 } == true
				"Bolívares" -> paymentState.amountBs.toFloatOrNull()?.let { it > 0 } == true &&
						reference.isNotBlank()
				
				"Mixto" -> paymentState.amountDollar.toFloatOrNull()?.let { it > 0 } == true &&
						paymentState.amountBs.toFloatOrNull()?.let { it > 0 } == true &&
						reference.isNotBlank()
				
				else -> false
			}
	
	LaunchedEffect(true) {
		viewModel.getPricesValue()
	}
	
	DisposableEffect(Unit) {
		onDispose { viewModel.resetPaymentState() }
	}
	
	if (showSnackbar) {
		LaunchedEffect(Unit) {
			delay(2000)
			showSnackbar = false
		}
	}
	
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = "Registro de pagos",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				),
				navigationIcon = {
					IconButton(onClick = { navController.navigate(AppRoutes.MainScreen) }) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = colorScheme.onPrimary
						)
					}
				}
			)
		},
		snackbarHost = {
			if (showSnackbar) {
				Snackbar(modifier = Modifier.padding(8.dp)) {
					Text(text = snackbarMessage)
				}
			}
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(innerPadding)
				.padding(8.dp)
				.verticalScroll(rememberScrollState())
		) {
			OutlinedTextField(
				value = nameUser,
				maxLines = 1,
				onValueChange = { nameUser = it },
				label = { Text("Nombre y apellido") },
				modifier = Modifier.fillMaxWidth()
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			// Frecuencia Dropdown
			ExposedDropdownMenuBox(
				expanded = isDropdownExpanded,
				onExpandedChange = { isDropdownExpanded = it }
			) {
				OutlinedTextField(
					value = paymentState.frequency,
					onValueChange = {},
					label = { Text("Frecuencia de pago") },
					modifier = Modifier
						.fillMaxWidth()
						.menuAnchor(),
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
					}
				)
				ExposedDropdownMenu(
					expanded = isDropdownExpanded,
					onDismissRequest = { isDropdownExpanded = false }
				) {
					paymentState.ListMembership.keys.forEach { frequency ->
						DropdownMenuItem(
							text = {
								Column {
									Text(frequency)
									Text(
										text = "Valor: ${paymentState.ListMembership[frequency]}$",
										fontSize = 12.sp,
										color = colorScheme.onSurfaceVariant
									)
								}
							},
							onClick = {
								viewModel.updatePaymentFrequency(frequency)
								isDropdownExpanded = false
							}
						)
					}
				}
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			// Tipo de pago Dropdown
			ExposedDropdownMenuBox(
				expanded = isTypeDropdownExpanded && isPaymentTypeEnabled,
				onExpandedChange = { if (isPaymentTypeEnabled) isTypeDropdownExpanded = it }
			) {
				OutlinedTextField(
					value = paymentState.type,
					onValueChange = {},
					label = { Text("Tipo de pago") },
					modifier = Modifier
						.fillMaxWidth()
						.menuAnchor(),
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(
							expanded = isTypeDropdownExpanded && isPaymentTypeEnabled
						)
					},
					enabled = isPaymentTypeEnabled
				)
				if (isPaymentTypeEnabled) {
					ExposedDropdownMenu(
						expanded = isTypeDropdownExpanded,
						onDismissRequest = { isTypeDropdownExpanded = false }
					) {
						paymentTypes.forEach { type ->
							DropdownMenuItem(
								text = { Text(type) },
								onClick = {
									viewModel.updatePaymentType(type)
									isTypeDropdownExpanded = false
								}
							)
						}
					}
				}
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			// Campos según tipo
			when (paymentState.type) {
				"Dólares" -> {
					OutlinedTextField(
						value = paymentState.amountDollar,
						maxLines = 1,
						onValueChange = { viewModel.updateAmountDollar(it) },
						label = { Text("Monto ($)") },
						modifier = Modifier.fillMaxWidth(),
						enabled = paymentState.frequency.isEmpty(),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
					)
				}
				
				"Bolívares" -> {
					OutlinedTextField(
						value = paymentState.amountBs,
						maxLines = 1,
						onValueChange = { viewModel.updateAmountBs(it) },
						label = { Text("Monto (Bs)") },
						modifier = Modifier.fillMaxWidth(),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
					)
				}
				
				"Mixto" -> {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						OutlinedTextField(
							value = paymentState.amountDollar,
							onValueChange = { viewModel.updateAmountDollar(it) },
							label = { Text("$") },
							maxLines = 1,
							modifier = Modifier.weight(1f),
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
						)
						OutlinedTextField(
							value = paymentState.amountBs,
							onValueChange = { viewModel.updateAmountBs(it) },
							label = { Text("Bs") },
							maxLines = 1,
							modifier = Modifier.weight(1f),
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
						)
					}
				}
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			if (paymentState.type != "Dólares") {
				OutlinedTextField(
					value = reference,
					maxLines = 1,
					onValueChange = { reference = it },
					label = { Text("Referencia") },
					modifier = Modifier.fillMaxWidth(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				Spacer(modifier = Modifier.height(8.dp))
			}
			
			OutlinedTextField(
				value = description,
				maxLines = 1,
				onValueChange = { description = it },
				label = { Text("Descripción") },
				modifier = Modifier.fillMaxWidth()
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Button(
				onClick = {
					val paymentData = when (paymentState.type) {
						"Dólares" -> "$${paymentState.amountDollar}"
						"Bolívares" -> "Bs${paymentState.amountBs}"
						"Mixto" -> "$${paymentState.amountDollar} + Bs${paymentState.amountBs}"
						else -> ""
					}
					snackbarMessage = "Pago registrado correctamente: $paymentData"
					showSnackbar = true
				},
				enabled = formIsValid,
				colors = ButtonDefaults.buttonColors(
					containerColor = colorScheme.primary,
					contentColor = colorScheme.onPrimary,
					disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
					disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
				),
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp)
			) {
				Text(text = "Guardar")
			}
		}
	}
}
/*@Preview(showBackground = true)
@Composable
fun PaymentsScreenPreview() {
	PaymentsScreen(
		navBottom = NavController(LocalContext.current),
		
		)
}*/