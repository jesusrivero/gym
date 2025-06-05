package com.techcode.gymcontrol.presentation.ui.settings.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.presentation.theme.GymTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
	navController: NavController,
) {
	GymTheme {
		SecurityContent(
			navBottom = navController
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityContent(
	navBottom: NavController,
) {
	var currentPassword by remember { mutableStateOf("") }
	var newPassword by remember { mutableStateOf("") }
	var confirmPassword by remember { mutableStateOf("") }
	var showCurrentPassword by remember { mutableStateOf(false) }
	var showNewPassword by remember { mutableStateOf(false) }
	var showConfirmPassword by remember { mutableStateOf(false) }
	var twoStepVerification by remember { mutableStateOf(false) }
	var appLockEnabled by remember { mutableStateOf(false) }
	var passwordError by remember { mutableStateOf(false) }

	fun validatePasswords(): Boolean {
		val isValid = newPassword == confirmPassword
		passwordError = !isValid && confirmPassword.isNotEmpty()
		return isValid
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Seguridad",
						color = Color.White,
						fontWeight = FontWeight.Medium
					)
				},
				navigationIcon = {
					IconButton(
						onClick = { navBottom.popBackStack() }
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				)
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {

			OutlinedTextField(
				value = currentPassword,
				maxLines = 1,
				onValueChange = { currentPassword = it },
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp),
				label = { Text("Contraseña actual") },
				placeholder = { Text("Ingresa tu contraseña actual") },
				visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
				trailingIcon = {
					IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
						Icon(
							painter = painterResource(
								id = if (showCurrentPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility
							),
							contentDescription = if (showCurrentPassword) "Ocultar contraseña" else "Mostrar contraseña",
							modifier = Modifier.size(22.dp)
						)
					}
				},
				leadingIcon = {
					Icon(
						Icons.Default.Lock,
						contentDescription = "Confirmar contraseña",
						modifier = Modifier.size(20.dp)
					)
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
			)

			OutlinedTextField(
				value = newPassword,
				maxLines = 1,
				onValueChange = {
					newPassword = it
					if (passwordError) validatePasswords()
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp),
				label = { Text("Nueva contraseña") },
				placeholder = { Text("Ingresa tu nueva contraseña") },
				visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
				trailingIcon = {
					IconButton(onClick = { showNewPassword = !showNewPassword }) {
						Icon(
							painter = painterResource(
								id = if (showNewPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility
							),
							contentDescription = if (showNewPassword) "Ocultar contraseña" else "Mostrar contraseña",
							modifier = Modifier.size(22.dp)
						)
					}
				},
				leadingIcon = {
					Icon(
						Icons.Default.Lock,
						contentDescription = "Confirmar contraseña",
						modifier = Modifier.size(20.dp)
					)
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
			)

			OutlinedTextField(
				value = confirmPassword,
				maxLines = 1,
				onValueChange = {
					confirmPassword = it
					if (it.isNotEmpty()) {
						passwordError = it != newPassword
					} else {
						passwordError = false
					}
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 16.dp),
				label = { Text("Confirmar nueva contraseña") },
				placeholder = { Text("Confirma tu nueva contraseña") },
				visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
				isError = passwordError,
				supportingText = {
					if (passwordError) {
						Text(
							text = "Las contraseñas no coinciden",
							color = Color.Red
						)
					}
				},
				trailingIcon = {
					IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
						Icon(
							painter = painterResource(
								id = if (showConfirmPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility
							),
							contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña",
							modifier = Modifier.size(22.dp)
						)
					}
				},
				leadingIcon = {
					Icon(
						Icons.Default.Lock,
						contentDescription = "Confirmar contraseña",
						modifier = Modifier.size(20.dp)
					)
				},
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
			)

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 24.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = "Verificación en dos pasos",
						fontSize = 16.sp
					)
					Switch(
						checked = twoStepVerification,
						onCheckedChange = { twoStepVerification = it }
					)
				}

				Spacer(modifier = Modifier.height(16.dp))

				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						text = "Bloquear app con PIN",
						fontSize = 16.sp
					)
					Switch(
						checked = appLockEnabled,
						onCheckedChange = { appLockEnabled = it }
					)
				}
			}

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 32.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Button(
					onClick = {
						if (validatePasswords()) {
							//  lógica para guardar los cambios
						}
					},
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color(0xCD4CAF50)
					)
				) {
					Text("Guardar cambios", color = Color.White)
				}

				Spacer(modifier = Modifier.height(16.dp))

				Button(
					onClick = { /* Eliminar cuenta */ },
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color.Red.copy(alpha = 0.8f)
					)
				) {
					Text("Eliminar cuenta", color = Color.White)
				}
			}
		}
	}
}