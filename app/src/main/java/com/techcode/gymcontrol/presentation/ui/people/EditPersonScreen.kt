package com.techcode.gymcontrol.presentation.ui.people


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.domain.model.Person
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonScreen(
	navController: NavController,
	viewModel: PeopleViewModel,
	id: Int,
	usuario: String? = null,
	email: String? = null,
	cedula: String? = null,
	numeroTelefono: String? = null
) {
	val colorScheme = MaterialTheme.colorScheme
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }

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
						text = "Editar Usuario",
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
							painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
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
		EditPersonContent(
			modifier = Modifier.padding(paddingValues),
			viewModel = viewModel,
			navController = navController,
			id = id,
			initialUsuario = usuario.orEmpty(),
			initialEmail = email.orEmpty(),
			initialCedula = cedula.orEmpty(),
			initialNumeroTelefono = numeroTelefono.orEmpty(),
			showSnackbar = { message ->
				snackbarMessage = message
				showSnackbar = true
			}
		)
	}
}

@Composable
fun EditPersonContent(
	modifier: Modifier = Modifier,
	viewModel: PeopleViewModel,
	navController: NavController,
	id: Int,
	initialUsuario: String,
	initialEmail: String,
	initialCedula: String,
	initialNumeroTelefono: String,
	showSnackbar: (String) -> Unit
) {
	var usuario by remember { mutableStateOf(initialUsuario) }
	var email by remember { mutableStateOf(initialEmail) }
	var cedula by remember { mutableStateOf(initialCedula) }
	var numeroTelefono by remember { mutableStateOf(initialNumeroTelefono) }

	var emailTouched by remember { mutableStateOf(false) }
	val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

	val formIsValid = usuario.isNotBlank() &&
			email.isNotBlank() &&
			cedula.isNotBlank() &&
			numeroTelefono.isNotBlank() &&
			isEmailValid

	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier
			.padding(16.dp)
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(
			value = usuario,
			onValueChange = { usuario = it },
			label = { Text("Nombre y apellido") },
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = email,
			onValueChange = {
				email = it
				emailTouched = true
			},
			label = { Text("Correo electrónico") },
			isError = emailTouched && !isEmailValid,
			modifier = Modifier.fillMaxWidth()
		)

		if (emailTouched && !isEmailValid) {
			Text(
				text = "Correo electrónico inválido",
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.labelSmall,
				modifier = Modifier
					.align(Alignment.Start)
					.padding(start = 16.dp, top = 4.dp)
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = cedula,
			onValueChange = { cedula = it },
			label = { Text("Cédula de identidad") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = numeroTelefono,
			onValueChange = { numeroTelefono = it },
			label = { Text("Número de teléfono") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				if (!isEmailValid) {
					emailTouched = true
					return@Button
				}

				val updatedPerson = Person(
					id = id,
					usuario = usuario,
					email = email,
					cedula = cedula,
					numeroTelefono = numeroTelefono
				)
				viewModel.updateUser(updatedPerson)

				showSnackbar("Cambios guardados correctamente")
				navController.popBackStack()
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
			Text("Guardar cambios", style = MaterialTheme.typography.labelLarge)
		}
	}
}




