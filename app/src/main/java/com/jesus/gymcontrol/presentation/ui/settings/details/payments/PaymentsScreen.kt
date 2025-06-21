package com.jesus.gymcontrol.presentation.ui.settings.details.payments


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel
import com.jesus.gymcontrol.domain.viewmodels.PaymentsViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserListViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun PaymentsScreen(navController: NavController) {
	PaymentsScreenContent(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreenContent(
	navController: NavController,
	viewModel: PaymentsViewModel = hiltViewModel()
) {
	val colorScheme = MaterialTheme.colorScheme
	val usersViewModel: UserListViewModel = hiltViewModel()
	val paymentState by viewModel.paymentState.collectAsState()
	val membershipViewModel: MembershipViewModel = hiltViewModel()
	val memberships = membershipViewModel.memberships
	val users by remember { derivedStateOf { usersViewModel.listUsers } }
	
	var description by remember { mutableStateOf("") }
	var nameUser by remember { mutableStateOf("") }
	var reference by remember { mutableStateOf("") }
	var isDropdownExpanded by remember { mutableStateOf(false) }
	var isTypeDropdownExpanded by remember { mutableStateOf(false) }
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	var selectedUserId by remember { mutableStateOf<String?>(null) }
	
	val paymentTypes = listOf("Dólares", "Bolívares", "Mixto")
	val isPaymentTypeEnabled = paymentState.frequency.isNotEmpty()
	
	val context = LocalContext.current
	val sessionManager = remember { SessionManager(context) }
	val gimnasioCode = sessionManager.getGymCode()
	
	val filteredUsers = if (nameUser.isBlank()) emptyList() else {
		users.filter {
			it.name.contains(nameUser, true) ||
					it.idCard.contains(nameUser, true) ||
					it.email.contains(nameUser, true) ||
					it.phone.contains(nameUser, true)
		}
	}
	
	val formIsValid = selectedUserId != null &&
			paymentState.frequency.isNotBlank() &&
			paymentState.type.isNotBlank() &&
			when (paymentState.type) {
				"Dólares" -> paymentState.listMembership[paymentState.frequency]?.let { it > 0 } == true
				"Bolívares" -> paymentState.amountBs.toFloatOrNull()?.let { it > 0 } == true && reference.isNotBlank()
				"Mixto" -> paymentState.amountDollar.toFloatOrNull()?.let { it > 0 } == true &&
						paymentState.amountBs.toFloatOrNull()?.let { it > 0 } == true &&
						reference.isNotBlank()
				else -> false
			}
	
	LaunchedEffect(viewModel.isSuccess) {
		if (viewModel.isSuccess) {
			snackbarMessage = "Pago registrado correctamente"
			showSnackbar = true
			viewModel.resetState()
		}
	}
	
	LaunchedEffect(viewModel.errorMessage) {
		viewModel.errorMessage?.let {
			snackbarMessage = "Error: $it"
			showSnackbar = true
			viewModel.resetState()
		}
	}
	
	LaunchedEffect(Unit) {
		membershipViewModel.loadMemberships()
		usersViewModel.loadUsers()
	}
	
	LaunchedEffect(memberships) {
		if (memberships.isNotEmpty()) {
			viewModel.setMemberships(memberships)
		}
	}
	
	DisposableEffect(Unit) {
		onDispose { viewModel.resetState() }
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
				colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primary),
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
				onValueChange = {
					nameUser = it
					selectedUserId = null
				},
				label = { Text("Nombre y apellido") },
				modifier = Modifier.fillMaxWidth(),
				trailingIcon = {
					if (selectedUserId != null) {
						IconButton(onClick = {
							nameUser = ""
							selectedUserId = null
						}) {
							Icon(Icons.Default.Close, contentDescription = "Borrar selección")
						}
					}
				}
			)
			
			if (filteredUsers.isNotEmpty() && selectedUserId == null) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.background(colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
						.padding(8.dp)
				) {
					filteredUsers.forEach { user ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									nameUser = user.name
									selectedUserId = user.id
								}
								.padding(vertical = 8.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Column {
								Text(user.name, fontWeight = FontWeight.Bold)
								Text(user.idCard, fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
							}
							Icon(Icons.Default.Person, contentDescription = null)
						}
					}
				}
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			ExposedDropdownMenuBox(
				expanded = isDropdownExpanded,
				onExpandedChange = { isDropdownExpanded = it }
			) {
				OutlinedTextField(
					value = paymentState.frequency,
					onValueChange = {},
					label = { Text("Frecuencia de pago") },
					modifier = Modifier.fillMaxWidth().menuAnchor(),
					readOnly = true,
					trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) }
				)
				ExposedDropdownMenu(
					expanded = isDropdownExpanded,
					onDismissRequest = { isDropdownExpanded = false }
				) {
					memberships.forEach { membership ->
						DropdownMenuItem(
							text = {
								Column {
									Text(membership.nombre)
									Text("Valor: ${membership.precio} $", fontSize = 12.sp, color = colorScheme.onSurfaceVariant)
								}
							},
							onClick = {
								viewModel.updatePaymentFrequency(membership.nombre)
								isDropdownExpanded = false
							}
						)
					}
				}
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			ExposedDropdownMenuBox(
				expanded = isTypeDropdownExpanded && isPaymentTypeEnabled,
				onExpandedChange = { if (isPaymentTypeEnabled) isTypeDropdownExpanded = it }
			) {
				OutlinedTextField(
					value = paymentState.type,
					onValueChange = {},
					label = { Text("Tipo de pago") },
					modifier = Modifier.fillMaxWidth().menuAnchor(),
					readOnly = true,
					trailingIcon = {
						ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeDropdownExpanded && isPaymentTypeEnabled)
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
			
			LaunchedEffect(paymentState.type, paymentState.frequency) {
				if (paymentState.type == "Mixto") {
					val price = paymentState.listMembership[paymentState.frequency] ?: 0.0
					if (paymentState.amountDollar.isBlank() || paymentState.amountDollar == "0") {
						viewModel.updateAmountDollar(price.toString())
					}
				}
			}
			
			when (paymentState.type) {
				"Dólares" -> {
					val price = paymentState.listMembership[paymentState.frequency]?.toString() ?: "0"
					OutlinedTextField(
						value = price,
						onValueChange = {},
						label = { Text("Monto ($)") },
						readOnly = true,
						modifier = Modifier.fillMaxWidth(),
						colors = OutlinedTextFieldDefaults.colors(
							disabledBorderColor = colorScheme.outline,
							disabledTextColor = colorScheme.onSurface,
							disabledLabelColor = colorScheme.onSurfaceVariant
						),
						enabled = false
					)
				}
				"Bolívares" -> {
					OutlinedTextField(
						value = paymentState.amountBs,
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
							label = { Text("Monto ($)") },
							modifier = Modifier.weight(1f),
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
						)
						OutlinedTextField(
							value = paymentState.amountBs,
							onValueChange = { viewModel.updateAmountBs(it) },
							label = { Text("Monto (Bs)") },
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
					onValueChange = { reference = it },
					label = { Text("Referencia") },
					modifier = Modifier.fillMaxWidth(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				Spacer(modifier = Modifier.height(8.dp))
			}
			
			OutlinedTextField(
				value = description,
				onValueChange = { description = it },
				label = { Text("Descripción") },
				modifier = Modifier.fillMaxWidth()
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Button(
				onClick = {
					val user = users.find { it.id == selectedUserId }
					val membership = memberships.find { it.nombre == paymentState.frequency }
					if (user != null && membership != null) {
						val montoDolares = paymentState.amountDollar.toDoubleOrNull() ?: 0.0
						val montoBs = paymentState.amountBs.toDoubleOrNull() ?: 0.0
						
						val montoTotal = when (paymentState.type) {
							"Dólares" -> paymentState.listMembership[paymentState.frequency] ?: 0.0
							"Bolívares" -> montoBs
							"Mixto" -> montoDolares + montoBs
							else -> 0.0
						}
						
						val pago = Pago(
							id = UUID.randomUUID().toString(),
							userId = user.id,
							name = user.name,
							idcard = user.idCard,
							membershipId = membership.id,
							membershipName = membership.nombre,
							tipepayment = paymentState.type,
							amount = montoTotal,
							amountDollar = if (paymentState.type != "Bolívares") montoDolares else null,
							amountBs = if (paymentState.type != "Dólares") montoBs else null,
							description = description,
							reference = if (paymentState.type != "Dólares") reference else null,
							date = System.currentTimeMillis(),
							gimnasioCode = gimnasioCode.toString()
						)
						
						viewModel.addPago(pago)
					} else {
						snackbarMessage = "Error: datos de usuario o membresía no encontrados"
						showSnackbar = true
					}
				}
			) {
			Text(text="Guardar")
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