package com.jesus.gymcontrol.presentation.ui.people


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.RegisterUserFromAdminViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.ui.commons.countryCodes
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonScreen(
	navController: NavController,
	viewModel: RegisterUserFromAdminViewModel = hiltViewModel(),
	sessionManager: SessionManager
) {
	val colorScheme = MaterialTheme.colorScheme
	
	val isRegistering = viewModel.isRegistering
	val registerSuccess = viewModel.registerSuccess
	val errorMessage = viewModel.errorMessage
	var showSuccessDialog by remember { mutableStateOf(false) }
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	var clearFormTrigger by remember { mutableStateOf(false) }
	
	
	if (showSnackbar) {
		LaunchedEffect(showSnackbar) {
			delay(2000)
			showSnackbar = false
		}
	}
	
	LaunchedEffect(registerSuccess) {
		if (registerSuccess) {
			showSuccessDialog = true
			viewModel.resetRegisterState()
		}
	}
	
	if (showSuccessDialog) {
		AlertDialog(
			onDismissRequest = { showSuccessDialog = false },
			confirmButton = {
				TextButton(
					onClick = {
						// 👉 Usuario elige NO registrar otro → Navegar
						showSuccessDialog = false
						navController.navigate(AppRoutes.PersonasScreen.route) {
							popUpTo(AppRoutes.RegPersonScreen) { inclusive = true }
						}
					}
				) {
					Text("No")
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						// 👉 Usuario elige SÍ registrar otro → limpiar formulario
						showSuccessDialog = false
						clearFormTrigger = true
					}
				) {
					Text("Sí")
				}
			},
			title = { Text("¡Éxito!") },
			text = { Text("Cliente registrado correctamente.\n¿Deseas registrar otra persona?") },
			icon = {
				Icon(
					imageVector = Icons.Default.CheckCircle,
					contentDescription = null,
					tint = Color(0xFF4CAF50)
				)
			},
			containerColor = MaterialTheme.colorScheme.surface,
			titleContentColor = MaterialTheme.colorScheme.onSurface,
			textContentColor = MaterialTheme.colorScheme.onSurface
		)
	}
	
	LaunchedEffect(errorMessage) {
		errorMessage?.let {
			snackbarMessage = it
			showSnackbar = true
			viewModel.resetRegisterState()
		}
	}
	
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = "Registro de clientes",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = colorScheme.primary
				),
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(
							painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = colorScheme.onPrimary
						)
					}
				}
			)
		},
		snackbarHost = {
			if (showSnackbar) {
				Snackbar(
					modifier = Modifier.padding(8.dp)
				) {
					Text(text = snackbarMessage)
				}
			}
		}
	) { paddingValues ->
		RegPersonContent(
			modifier = Modifier.padding(paddingValues),
			viewModel = viewModel,
			isLoading = isRegistering,
			sessionManager = sessionManager,
			clearFormTrigger = clearFormTrigger,
			onFormCleared = {
				clearFormTrigger = false
			}
		)
	}
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonContent(
	modifier: Modifier = Modifier,
	viewModel: RegisterUserFromAdminViewModel,
	aviewModel: AuthViewModel = hiltViewModel(),
	gviewModel: GymViewModel = hiltViewModel(),
	clearFormTrigger: Boolean,
	onFormCleared: () -> Unit,
	sessionManager: SessionManager,
	isLoading: Boolean,
) {
	val colorScheme = MaterialTheme.colorScheme
	
	var name by rememberSaveable { mutableStateOf("") }
	var lastname by rememberSaveable { mutableStateOf("") }
	var email by rememberSaveable { mutableStateOf("") }
	var password by rememberSaveable { mutableStateOf("") }
	var phone by rememberSaveable { mutableStateOf("") }
	var idCard by rememberSaveable { mutableStateOf("") }
	var code by rememberSaveable { mutableStateOf("") }
	var gimnasioCode by rememberSaveable { mutableStateOf("") }
	var rol by rememberSaveable { mutableStateOf("cliente") }
	val date by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
	var showDialog by remember { mutableStateOf(false) }
	var idCardError by rememberSaveable { mutableStateOf<String?>(null) }
	val onlyLettersRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*\$")
	val trimmedEmail = email.trim()
	val isNameValid = name.isNotBlank()
	val islastNameValid = lastname.isNotBlank()
	val isEmailValid =
		trimmedEmail.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
	val isPasswordValid = password.length >= 6
	val isIdCardValid = idCard.length in 7..9
	val isCodeValid = code.isNotBlank()
	var selectedCountryCode by rememberSaveable { mutableStateOf("58") }
	var isCountryDropdownExpanded by rememberSaveable { mutableStateOf(false) }
	val fullPhone = "$selectedCountryCode$phone"
	val isPhoneValid = phone.isBlank() || fullPhone.matches(Regex("^[1-9]\\d{7,14}$"))
	val formIsValid =
		isNameValid && islastNameValid && isEmailValid && isPasswordValid && isCodeValid && isIdCardValid && isPhoneValid
	val configuration = LocalConfiguration.current
	val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
	val currentUserRole = gviewModel.currentUserRole
	val rolesDisponibles =
		if (currentUserRole == "dueño") listOf("cliente", "administrador") else listOf("cliente")
	var isRolDropdownExpanded by rememberSaveable { mutableStateOf(false) }
	val userUid = FirebaseAuth.getInstance().currentUser?.uid
	val currentRole = sessionManager.getRol()?.lowercase()
	
	LaunchedEffect(userUid) {
		userUid?.let {
			gviewModel.loadCurrentUserRole(it)
		}
	}
	
	if (clearFormTrigger) {
		name = ""
		lastname = ""
		email = ""
		password = ""
		phone = ""
		idCard = ""
		code = ""
		rol = "cliente"
		onFormCleared()
	}
	
	
	if (showDialog) {
		AlertDialog(
			onDismissRequest = { showDialog = false },
			confirmButton = {
				TextButton(
					onClick = {
						showDialog = false
						val user = UserRegistrationData(
							email = trimmedEmail,
							password = password,
							name = name,
							lastname = lastname,
							phone = if (phone.isBlank()) null else fullPhone,
							idCard = idCard,
							gender = "",
							age = 0,
							rol = rol,
							membership = "",
							code = code,
							gimnasioCode = gimnasioCode,
							date = date
						)
						viewModel.registerUserAsAdmin(user)
					}
				) { Text("Confirmar") }
			},
			dismissButton = {
				TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
			},
			containerColor = MaterialTheme.colorScheme.surface,
			titleContentColor = MaterialTheme.colorScheme.onSurface,
			textContentColor = MaterialTheme.colorScheme.onSurface,
			title = { Text("Confirmar registro") },
			text = { Text("¿Deseas registrar a esta persona con rol '$rol'?") }
		)
	}
	
	Column(
		modifier = modifier
			.padding(16.dp)
			.fillMaxWidth()
			.let { base ->
				if (isLandscape) base.verticalScroll(rememberScrollState()) else base
			},
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(2.dp),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background)
		) {
			Column(modifier = Modifier.padding(12.dp)) {
				OutlinedTextField(
					value = name,
					onValueChange = {
						if (it.matches(onlyLettersRegex)) name = it
					},
					label = { Text("Nombre") },
					modifier = Modifier.fillMaxWidth()
				)
				if (!isNameValid && name.isNotEmpty()) {
					Text(
						"El nombre es obligatorio",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				
				OutlinedTextField(
					value = lastname,
					onValueChange = {
						if (it.matches(onlyLettersRegex)) lastname = it
					},
					label = { Text("Apellido") },
					modifier = Modifier.fillMaxWidth()
				)
				if (!isNameValid && lastname.isNotEmpty()) {
					Text(
						"El nombre es obligatorio",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
			}
		}
		
		Spacer(modifier = Modifier.height(12.dp))
		
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(2.dp),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background)
		) {
			Column(modifier = Modifier.padding(12.dp)) {
				OutlinedTextField(
					value = email,
					onValueChange = { email = it },
					label = { Text("Correo electrónico") },
					modifier = Modifier.fillMaxWidth()
				)
				if (email.isNotBlank() && !isEmailValid) {
					Text(
						"Debe ser un correo válido",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				
				OutlinedTextField(
					value = password,
					onValueChange = { password = it },
					label = { Text("Contraseña (mín. 6 caracteres)") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					modifier = Modifier.fillMaxWidth()
				)
				if (password.isNotEmpty() && !isPasswordValid) {
					Text(
						"Debe tener al menos 6 caracteres",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				
				OutlinedTextField(
					value = idCard,
					onValueChange = {
						idCard = it
						idCardError = null
					},
					label = { Text("Cédula") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					modifier = Modifier.fillMaxWidth()
				)
				if (idCard.isNotEmpty() && !isIdCardValid) {
					Text(
						"La cédula debe tener entre 7 y 9 dígitos",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				if (idCardError != null) {
					Text(
						idCardError!!,
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				
				
				
				Spacer(modifier = Modifier.height(8.dp))
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					ExposedDropdownMenuBox(
						expanded = isCountryDropdownExpanded,
						onExpandedChange = { isCountryDropdownExpanded = it },
						modifier = Modifier.weight(0.3f) // antes: 0.5f
					) {
						OutlinedTextField(
							readOnly = true,
							value = "+$selectedCountryCode",
							onValueChange = {},
							label = { Text("País") },
							singleLine = true, // 👈 evita salto de línea
							trailingIcon = {
								ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCountryDropdownExpanded)
							},
							modifier = Modifier
								.menuAnchor()
								.fillMaxWidth()
						)
						ExposedDropdownMenu(
							expanded = isCountryDropdownExpanded,
							modifier = Modifier
								.background(MaterialTheme.colorScheme.surfaceVariant),
							onDismissRequest = { isCountryDropdownExpanded = false }
						) {
							countryCodes.forEach { (label, code) ->
								DropdownMenuItem(
									text = { Text(label) },
									onClick = {
										selectedCountryCode = code
										isCountryDropdownExpanded = false
									}
								)
							}
						}
					}
					
					Spacer(modifier = Modifier.width(8.dp))
					
					OutlinedTextField(
						value = phone,
						onValueChange = { phone = it },
						label = { Text("Número") },
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
						singleLine = true,
						modifier = Modifier.weight(0.7f), // antes: 1f
						isError = phone.isNotBlank() && !isPhoneValid
					)
				}
				
				Spacer(modifier = Modifier.height(8.dp))
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					OutlinedTextField(
						value = code,
						onValueChange = { code = it },
						label = { Text("Código") },
						isError = !isCodeValid && code.isNotEmpty(),
						modifier = Modifier.weight(1f)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Button(
						onClick = {
							viewModel.SetSelectedRoleForCode(rol)
							viewModel.generateCodeForRoleFromAdmin { generated ->
								code = generated
							}
						}
					) {
						Text("Generar")
					}
				}
				if (!isCodeValid && code.isNotEmpty()) {
					Text(
						"El código es obligatorio",
						color = colorScheme.error,
						style = MaterialTheme.typography.labelSmall
					)
				}
				
				// Donde defines el campo de rol actual, quítalo y reemplaza con esto:
				if (currentRole == "dueño") {
					ExposedDropdownMenuBox(
						expanded = isRolDropdownExpanded,
						onExpandedChange = { isRolDropdownExpanded = it },
						modifier = Modifier.fillMaxWidth()
					) {
						OutlinedTextField(
							readOnly = true,
							value = if (rol.isNotEmpty()) rol.replaceFirstChar { it.uppercase() } else "",
							onValueChange = {},
							label = { Text("Rol") },
							trailingIcon = {
								ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRolDropdownExpanded)
							},
							modifier = Modifier.menuAnchor()
						)
						ExposedDropdownMenu(
							expanded = isRolDropdownExpanded,
							onDismissRequest = { isRolDropdownExpanded = false },
							modifier = Modifier
								.background(MaterialTheme.colorScheme.surfaceVariant)
						) {
							rolesDisponibles.forEach { rolOpcion ->
								DropdownMenuItem(
									text = { Text(rolOpcion.replaceFirstChar { it.uppercase() }) },
									onClick = {
										rol = rolOpcion
										isRolDropdownExpanded = false
									}
								)
							}
						}
					}
				} else {
					OutlinedTextField(
						value = rol,
						onValueChange = {},
						label = { Text("Rol") },
						readOnly = true,
						enabled = false,
						modifier = Modifier.fillMaxWidth()
					)
				}
				
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Button(
			onClick = {
				idCardError = null
				aviewModel.checkidcardExists(idCard) { exists ->
					if (exists) {
						idCardError = "La cédula ya está registrada"
					} else {
						showDialog = true
					}
				}
			},
			enabled = formIsValid && !isLoading && (currentUserRole != "dueño" || rol.isNotEmpty()),
			modifier = Modifier.fillMaxWidth()
		) {
			if (isLoading) {
				CircularProgressIndicator(
					color = Color.White,
					modifier = Modifier.size(20.dp)
				)
			} else {
				Text("Registrar", style = MaterialTheme.typography.labelLarge)
			}
			
		}
	}
	
}