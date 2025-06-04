package com.jesus.gymcontrol.presentation.ui.people
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonScreen(
	navController: NavController,
	viewModel: PeopleViewModel = hiltViewModel()
) {
	val colorScheme = MaterialTheme.colorScheme
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }

	if (showSnackbar) {
		LaunchedEffect(showSnackbar) {
			delay(2000)
			showSnackbar = false
		}
	}

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = "Gym Control",
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
			navController = navController,
			onShowSnackbar = { message ->
				snackbarMessage = message
				showSnackbar = true
			}
		)
	}
}

@Composable
fun RegPersonContent(
	modifier: Modifier = Modifier,
	viewModel: PeopleViewModel,
	navController: NavController,
	onShowSnackbar: (String) -> Unit
) {
	var usuario by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var cedula by remember { mutableStateOf("") }
	var numeroTelefono by remember { mutableStateOf("") }
	val colorScheme = MaterialTheme.colorScheme
	val isValidEmail = email.trim().matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
	val formIsValid = usuario.isNotBlank() &&
			email.isNotBlank() &&
			cedula.isNotBlank() &&
			numeroTelefono.isNotBlank() &&
			isValidEmail

	Column(
		modifier = modifier
			.padding(16.dp)
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(
			value = usuario,
			maxLines = 1,
			onValueChange = { usuario = it },
			label = { Text("Nombre y apellido") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = email,
			maxLines = 1,
			onValueChange = { email = it },
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
			placeholder = {
				Text("Correo electrónico", color = colorScheme.onSurfaceVariant)
			},
			singleLine = true,
			isError = email.isNotBlank() && !isValidEmail
		)

		if (email.isNotBlank() && !isValidEmail) {
			Text(
				text = "Debe ser un correo válido de Gmail",
				color = MaterialTheme.colorScheme.error,
				fontSize = 12.sp,
				modifier = Modifier
					.align(Alignment.Start)
					.padding(top = 4.dp)
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = cedula,
			maxLines = 1,
			onValueChange = { cedula = it },
			label = { Text("Cédula de identidad") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = numeroTelefono,
			maxLines = 1,
			onValueChange = { numeroTelefono = it },
			label = { Text("Número de teléfono") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				val person = Person(
					usuario = usuario,
					email = email,
					cedula = cedula,
					numeroTelefono = numeroTelefono
				)
				viewModel.saveUser(person)
				onShowSnackbar("Cliente registrado correctamente")
				navController.navigate(AppRoutes.MainScreen)
			},
			enabled = formIsValid,
			colors = ButtonDefaults.buttonColors(
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary,
				disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
				disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
			),
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Agregar", style = MaterialTheme.typography.labelLarge)
		}
	}
}


