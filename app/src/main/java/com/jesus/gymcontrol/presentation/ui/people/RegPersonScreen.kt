package com.jesus.gymcontrol.presentation.ui.people


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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.model.UserRegistrationData
import com.jesus.gymcontrol.domain.viewmodels.RegisterUserFromAdminViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonScreen(
	navController: NavController,
	viewModel: RegisterUserFromAdminViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	val context = LocalContext.current
	
	val isRegistering = viewModel.isRegistering
	val registerSuccess = viewModel.registerSuccess
	val errorMessage = viewModel.errorMessage
	
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	
	if (showSnackbar) {
		LaunchedEffect(showSnackbar) {
			delay(2000)
			showSnackbar = false
		}
	}
	
	LaunchedEffect(registerSuccess) {
		if (registerSuccess) {
			snackbarMessage = "Cliente registrado correctamente"
			showSnackbar = true
			viewModel.resetRegisterState()
			navController.navigate(AppRoutes.MainScreen) {
				popUpTo(AppRoutes.RegPersonScreen) { inclusive = true }
			}
		}
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
			isLoading = isRegistering
		)
	}
}

@Composable
fun RegPersonContent(
	modifier: Modifier = Modifier,
	viewModel: RegisterUserFromAdminViewModel,
	isLoading: Boolean,
) {
	val colorScheme = MaterialTheme.colorScheme
	
	var name by rememberSaveable { mutableStateOf("") }
	var email by rememberSaveable { mutableStateOf("") }
	var password by rememberSaveable { mutableStateOf("") }
	var phone by rememberSaveable { mutableStateOf("") }
	var idCard by rememberSaveable { mutableStateOf("") }
	var code by rememberSaveable { mutableStateOf("") }
	var gimnasioCode by rememberSaveable { mutableStateOf("") }
	var rol by rememberSaveable { mutableStateOf("cliente") }
	val date by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
	
	var showDialog by remember { mutableStateOf(false) }
	
	// Puedes luego mover esto a un validador aparte
	val isNameValid = name.isNotBlank()
	val isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
	val isPasswordValid = password.length >= 6
	val isIdCardValid = idCard.isNotBlank()
	val isCodeValid = code.isNotBlank()
	
	val formIsValid = isNameValid && isEmailValid && isPasswordValid && isCodeValid && isIdCardValid
	
	if (showDialog) {
		AlertDialog(
			onDismissRequest = { showDialog = false },
			confirmButton = {
				TextButton(
					onClick = {
						showDialog = false
						val user = UserRegistrationData(
							email = email,
							password = password,
							name = name,
							phone = phone,
							idCard = idCard,
							gender = "", // Puedes añadir campo luego si lo deseas
							age = 0, // Igual
							rol = rol,
							membership = "",
							code = code,
							gimnasioCode = gimnasioCode,
							date = date
						)
						viewModel.registerUserAsAdmin(user)
					}
				) {
					Text("Confirmar")
				}
			},
			dismissButton = {
				TextButton(onClick = { showDialog = false }) {
					Text("Cancelar")
				}
			},
			title = { Text("Confirmar registro") },
			text = { Text("¿Deseas registrar a esta persona con rol '$rol'?") }
		)
	}
	
	Column(
		modifier = modifier
			.padding(16.dp)
			.verticalScroll(rememberScrollState())
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				OutlinedTextField(
					value = name,
					onValueChange = { name = it },
					label = { Text("Nombre completo") },
					isError = !isNameValid && name.isNotEmpty(),
					maxLines = 1,
					modifier = Modifier.fillMaxWidth()
				)
				if (!isNameValid && name.isNotEmpty()) {
					Text("El nombre es obligatorio", color = MaterialTheme.colorScheme.error)
				}
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				
				
				OutlinedTextField(
					value = email,
					onValueChange = { email = it },
					label = { Text("Correo Gmail") },
					isError = email.isNotBlank() && !isEmailValid,
					maxLines = 1,
					modifier = Modifier.fillMaxWidth()
				)
				if (email.isNotBlank() && !isEmailValid) {
					Text("Correo inválido. Usa un Gmail válido", color = MaterialTheme.colorScheme.error)
				}
				
				OutlinedTextField(
					value = password,
					onValueChange = { password = it },
					label = { Text("Contraseña (mín. 6 caracteres)") },
					isError = password.isNotEmpty() && !isPasswordValid,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					maxLines = 1,
					modifier = Modifier.fillMaxWidth()
				)
				if (password.isNotEmpty() && !isPasswordValid) {
					Text("Debe tener al menos 6 caracteres", color = MaterialTheme.colorScheme.error)
				}
				
				OutlinedTextField(
					value = idCard,
					onValueChange = { idCard = it },
					label = { Text("Cédula") },
					isError = !isIdCardValid && idCard.isNotEmpty(),
					maxLines = 1,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					modifier = Modifier.fillMaxWidth()
				)
				if (!isIdCardValid && idCard.isNotEmpty()) {
					Text("La cédula es obligatoria", color = MaterialTheme.colorScheme.error)
				}
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					OutlinedTextField(
						value = code,
						onValueChange = { code = it },
						label = { Text("Código") },
						isError = !isCodeValid && code.isNotEmpty(),
						maxLines = 1,
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
					Text("El código es obligatorio", color = MaterialTheme.colorScheme.error)
				}
				
				OutlinedTextField(
					value = rol,
					onValueChange = {},
					label = { Text("Rol") },
					readOnly = true,
					modifier = Modifier.fillMaxWidth(),
					enabled = false,
					colors = OutlinedTextFieldDefaults.colors(
						disabledBorderColor = colorScheme.outline,
						disabledTextColor = colorScheme.onSurface,
						disabledLabelColor = colorScheme.onSurfaceVariant
					)
				)
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Button(
			onClick = { showDialog = true },
			enabled = formIsValid && !isLoading,
			modifier = Modifier.fillMaxWidth()
		) {
			if (isLoading)
				CircularProgressIndicator(
					color = Color.White,
					modifier = Modifier.size(20.dp)
				)
			else
				Text("Registrar", style = MaterialTheme.typography.labelLarge)
		}
	}
}