package com.jesus.gymcontrol.presentation.ui.settings.details.payments


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Pago
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel
import com.jesus.gymcontrol.domain.viewmodels.PaymentsViewModel
import com.jesus.gymcontrol.domain.viewmodels.PromotionViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserListViewModel
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
	viewModel: PaymentsViewModel = hiltViewModel(),
	promotionViewModel: PromotionViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	val usersViewModel: UserListViewModel = hiltViewModel()
	val paymentState by viewModel.paymentState.collectAsState()
	val membershipViewModel: MembershipViewModel = hiltViewModel()
	val memberships = membershipViewModel.memberships
	val users by remember { derivedStateOf { usersViewModel.listUsers } }
	
	// Estados locales
	var description by remember { mutableStateOf("") }
	var nameUser by remember { mutableStateOf("") }
	var reference by remember { mutableStateOf("") }
	var isMembershipDropdownExpanded by remember { mutableStateOf(false) }
	var isTypeDropdownExpanded by remember { mutableStateOf(false) }
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	var selectedUserId by remember { mutableStateOf<String?>(null) }
	var isPromoDropdownExpanded by remember { mutableStateOf(false) }
	val selectedPromotion = viewModel.selectedPromotion
	val promotions = promotionViewModel.promotions
	
	val paymentTypes = listOf("Dólares", "Bolívares", "Mixto")
	val isPaymentTypeEnabled = paymentState.frequency.isNotEmpty()
	
	val context = LocalContext.current
	val sessionManager = remember { SessionManager(context) }
	val gimnasioCode = sessionManager.getGymCode()
	
	val filteredUsers = if (nameUser.isBlank()) emptyList() else {
		users.filter { user ->
			user.rol == "cliente" && (
					user.name.contains(nameUser, true) ||
							user.idcard.contains(nameUser, true) ||
							user.email.contains(nameUser, true) ||
							user.phone.contains(nameUser, true)
					)
		}
	}
	
	val formIsValid = selectedUserId != null &&
			paymentState.frequency.isNotBlank() &&
			paymentState.type.isNotBlank() &&
			when (paymentState.type) {
				"Dólares" -> paymentState.amountDollar.toDoubleOrNull()?.let { it > 0 } == true
				"Bolívares" -> paymentState.amountBs.toDoubleOrNull()
					?.let { it > 0 } == true && reference.isNotBlank()
				
				"Mixto" -> paymentState.amountDollar.toDoubleOrNull()?.let { it > 0 } == true &&
						paymentState.amountBs.toDoubleOrNull()?.let { it > 0 } == true &&
						reference.isNotBlank()
				
				else -> false
			}
	
	LaunchedEffect(viewModel.isSuccess) {
		if (viewModel.isSuccess) {
			snackbarMessage = "Pago registrado correctamente"
			showSnackbar = true
			viewModel.resetState()
			navController.popBackStack()
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
		promotionViewModel.loadPromotions()
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
					IconButton(onClick = { navController.popBackStack() }) {
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
		},
		bottomBar = {
			BottomAppBar(
				containerColor = colorScheme.background,
				actions = {
					TextButton(
						onClick = {
							val user = users.find { it.id == selectedUserId }
							val membership = memberships.find { it.nombre == paymentState.frequency }
							if (user != null && membership != null) {
								val montoDolares = paymentState.amountDollar.toDoubleOrNull() ?: 0.0
								val montoBs = paymentState.amountBs.toDoubleOrNull() ?: 0.0
								
								val montoTotal = when (paymentState.type) {
									"Dólares" -> montoDolares
									"Bolívares" -> montoBs
									"Mixto" -> montoDolares + montoBs
									else -> 0.0
								}
								
								val pago = Pago(
									id = UUID.randomUUID().toString(),
									userId = user.id,
									name = user.name,
									idcard = user.idcard,
									membershipId = membership.id,
									membershipName = membership.nombre,
									tipepayment = paymentState.type,
									amount = montoTotal,
									amountDollar = if (paymentState.type != "Bolívares") montoDolares else null,
									amountBs = if (paymentState.type != "Dólares") montoBs else null,
									description = description,
									reference = if (paymentState.type != "Dólares") reference else null,
									date = System.currentTimeMillis(),
									gimnasioCode = gimnasioCode.toString(),
									promocionId = selectedPromotion?.id,
									promocionNombre = selectedPromotion?.nombre ?: "",
									promocionDescripcion = selectedPromotion?.descripcion ?: "",
									promocionDescuento = selectedPromotion?.porcentajeDescuento ?: 0.0
								)
								
								viewModel.addPago(pago)
							} else {
								snackbarMessage = "Error: datos de usuario o membresía no encontrados"
								showSnackbar = true
							}
						},
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp, vertical = 8.dp),
						enabled = formIsValid,
						colors = ButtonDefaults.buttonColors(
							containerColor = colorScheme.primary,
							disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f)
						)
					) {
						Text("Registrar Pago", fontWeight = FontWeight.Bold)
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(innerPadding)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.verticalScroll(rememberScrollState())
					.weight(1f)
			) {
				
				Spacer(modifier = Modifier.height(10.dp))
				
				// Sección búsqueda usuario
				Card(
					modifier = Modifier.fillMaxWidth(),
					elevation = CardDefaults.cardElevation(4.dp),
					colors = CardDefaults.cardColors(containerColor = colorScheme.background)
				) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(
							text = "Cliente",
							style = MaterialTheme.typography.titleSmall,
							color = colorScheme.primary,
							modifier = Modifier.padding(bottom = 8.dp)
						)
						
						OutlinedTextField(
							value = nameUser,
							onValueChange = {
								nameUser = it
								selectedUserId = null
							},
							label = { Text("Buscar..") },
							modifier = Modifier.fillMaxWidth(),
							leadingIcon = {
								Icon(
									Icons.Default.Search,
									contentDescription = "Buscar"
								)
							},
							maxLines = 1,
							trailingIcon = {
								if (selectedUserId != null) {
									IconButton(onClick = {
										nameUser = ""
										selectedUserId = null
										
										
										viewModel.resetState()
										description = ""
										reference = ""
										viewModel.selectedPromotion(null)
									}) {
										Icon(Icons.Default.Close, contentDescription = "Borrar selección")
									}
								}
							}
						)
						
						if (filteredUsers.isNotEmpty() && selectedUserId == null) {
							Spacer(modifier = Modifier.height(8.dp))
							LazyColumn(
								modifier = Modifier
									.fillMaxWidth()
									.heightIn(max = 200.dp)
									.background(colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
							) {
								items(filteredUsers) { user ->
									Row(
										modifier = Modifier
											.fillMaxWidth()
											.clickable {
												nameUser = user.name
												selectedUserId = user.id
											}
											.padding(12.dp),
										verticalAlignment = Alignment.CenterVertically,
										horizontalArrangement = Arrangement.spacedBy(8.dp)
									) {
										Icon(
											Icons.Default.Person,
											contentDescription = null,
											tint = colorScheme.primary
										)
										Column(modifier = Modifier.weight(1f)) {
											Text(
												user.name,
												fontWeight = FontWeight.Bold,
												maxLines = 1,
												overflow = TextOverflow.Ellipsis
											)
											Text(
												"CI: ${user.idcard}",
												fontSize = 12.sp,
												color = colorScheme.onSurfaceVariant
											)
										}
									}
									Divider(modifier = Modifier.padding(horizontal = 12.dp))
								}
							}
						}
					}
				}
				
				Spacer(modifier = Modifier.height(10.dp))
				
				// Detalles del pago
				Card(
					modifier = Modifier.fillMaxWidth(),
					elevation = CardDefaults.cardElevation(4.dp),
					colors = CardDefaults.cardColors(containerColor = colorScheme.background)
				) {
					Column(modifier = Modifier.padding(16.dp)) {
						Text(
							text = "Detalles del Pago",
							style = MaterialTheme.typography.titleSmall,
							color = colorScheme.primary,
							modifier = Modifier.padding(bottom = 8.dp)
						)
						
						// Selector de membresía
						
						ExposedDropdownMenuBox(
							expanded = isMembershipDropdownExpanded,
							onExpandedChange = { isMembershipDropdownExpanded = it }
						) {
							OutlinedTextField(
								value = paymentState.frequency,
								onValueChange = {},
								label = { Text("Tipo de membresía") },
								modifier = Modifier
									.fillMaxWidth()
									.menuAnchor(),
								readOnly = true,
								leadingIcon = {
									Icon(
										Icons.Default.FitnessCenter,
										contentDescription = "Membresía"
									)
								},
								trailingIcon = {
									ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMembershipDropdownExpanded)
								},
							)
							
							ExposedDropdownMenu(
								expanded = isMembershipDropdownExpanded,
								onDismissRequest = { isMembershipDropdownExpanded = false },
								modifier = Modifier
									.background(MaterialTheme.colorScheme.surfaceVariant)
							) {
								memberships.filter { it.state == "activo" }.forEach { membership ->
									DropdownMenuItem(
										text = {
											Column {
												Text(membership.nombre)
												Text(
													"Valor: ${membership.precio} $",
													fontSize = 12.sp,
													color = MaterialTheme.colorScheme.onSurfaceVariant
												)
											}
										},
										onClick = {
											viewModel.updatePaymentFrequency(membership.nombre)
											viewModel.calculateDiscountedAmountIfApplicable()
											isMembershipDropdownExpanded = false
										}
									)
								}
							}
						}
						
						
						Spacer(modifier = Modifier.height(10.dp))
						
						
						// Selector tipo de pago
						ExposedDropdownMenuBox(
							expanded = isTypeDropdownExpanded && isPaymentTypeEnabled,
							onExpandedChange = { if (isPaymentTypeEnabled) isTypeDropdownExpanded = it },
						) {
							OutlinedTextField(
								value = paymentState.type,
								onValueChange = {},
								label = { Text("Método de pago") },
								modifier = Modifier
									.fillMaxWidth()
									.menuAnchor(),
								readOnly = true,
								leadingIcon = {
									Icon(
										Icons.Default.Payments,
										contentDescription = "Tipo de pago"
									)
								},
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
									onDismissRequest = { isTypeDropdownExpanded = false },
									modifier = Modifier
										.background(MaterialTheme.colorScheme.surfaceVariant)
								) {
									paymentTypes.forEach { type ->
										DropdownMenuItem(
											text = { Text(type) },
											onClick = {
												viewModel.updatePaymentType(type)
												viewModel.calculateDiscountedAmountIfApplicable()
												isTypeDropdownExpanded = false
											},
										)
									}
								}
							}
						}
						
						Spacer(modifier = Modifier.height(10.dp))

//						 Dropdown promociones
						ExposedDropdownMenuBox(
							expanded = isPromoDropdownExpanded,
							onExpandedChange = { isPromoDropdownExpanded = it }
						) {
							OutlinedTextField(
								value = selectedPromotion?.nombre ?: "Sin promoción",
								onValueChange = {},
								label = { Text("Promoción (opcional)") },
								modifier = Modifier
									.fillMaxWidth()
									.menuAnchor(),
								readOnly = true,
								leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = "Promoción") },
								trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPromoDropdownExpanded) }
							)
							ExposedDropdownMenu(
								expanded = isPromoDropdownExpanded,
								onDismissRequest = { isPromoDropdownExpanded = false },
								modifier = Modifier
									.background(MaterialTheme.colorScheme.surfaceVariant)
							) {
								DropdownMenuItem(
									text = { Text("Sin promoción") },
									onClick = {
										viewModel.selectedPromotion(null ?: Promotion())
										isPromoDropdownExpanded = false
									}
								)
								promotions.filter { it.activo && it.gimnasioCode == gimnasioCode }
									.forEach { promo ->
										DropdownMenuItem(
											text = {
												Column {
													Text(promo.nombre)
													if (promo.descripcion.isNotBlank()) {
														Text(
															promo.descripcion,
															fontSize = 12.sp,
															color = colorScheme.onSurfaceVariant
														)
													}
													Text(
														"Descuento: ${promo.porcentajeDescuento}%",
														fontSize = 12.sp,
														color = colorScheme.primary
													)
												}
											},
											onClick = {
												viewModel.selectedPromotion(promo)
												viewModel.calculateDiscountedAmountIfApplicable()
												isPromoDropdownExpanded = false
											}
										)
									}
							}
						}
						
						Spacer(modifier = Modifier.height(10.dp))
						
						
						// Campos monto según tipo de pago
						when (paymentState.type) {
							"Dólares" -> {
								val price = paymentState.amountDollar.takeIf { it.isNotBlank() } ?: "0"
								OutlinedTextField(
									value = price,
									onValueChange = {},
									label = { Text("Monto en dólares") },
									readOnly = true,
									modifier = Modifier.fillMaxWidth(),
									leadingIcon = {
										Icon(
											Icons.Default.AttachMoney,
											contentDescription = "Monto en dólares"
										)
									},
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
									label = { Text("Monto en bolívares") },
									modifier = Modifier.fillMaxWidth(),
									leadingIcon = {
										Icon(
											painterResource(id = R.drawable.ic_details),
											contentDescription = "Monto en bolívares"
										)
									},
									keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
								)
							}
							
							"Mixto" -> {
								Column {
									OutlinedTextField(
										value = paymentState.amountDollar,
										onValueChange = { viewModel.updateAmountDollar(it) },
										label = { Text("Monto en dólares") },
										modifier = Modifier.fillMaxWidth(),
										leadingIcon = {
											Icon(
												Icons.Default.AttachMoney,
												contentDescription = "Monto en dólares"
											)
										},
										keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
									)
									Spacer(modifier = Modifier.height(8.dp))
									OutlinedTextField(
										value = paymentState.amountBs,
										onValueChange = { viewModel.updateAmountBs(it) },
										label = { Text("Monto en bolívares") },
										modifier = Modifier.fillMaxWidth(),
										leadingIcon = {
											Icon(
												painterResource(id = R.drawable.ic_details),
												contentDescription = "Monto en bolívares"
											)
										},
										keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
									)
								}
							}
						}
						
						Spacer(modifier = Modifier.height(10.dp))
						
						// Campo de referencia (excepto dólares)
						if (paymentState.type != "Dólares") {
							OutlinedTextField(
								value = reference,
								onValueChange = { reference = it },
								label = { Text("Número de referencia") },
								modifier = Modifier.fillMaxWidth(),
								leadingIcon = {
									Icon(
										Icons.Default.Numbers,
										contentDescription = "Referencia"
									)
								},
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
							)
							Spacer(modifier = Modifier.height(16.dp))
						}
						
						// Campo de descripción
						OutlinedTextField(
							value = description,
							onValueChange = { description = it },
							label = { Text("Notas adicionales") },
							modifier = Modifier.fillMaxWidth(),
							leadingIcon = {
								Icon(
									Icons.Default.AlternateEmail,
									contentDescription = "Descripción"
								)
							},
							maxLines = 2
						)
					}
				}
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