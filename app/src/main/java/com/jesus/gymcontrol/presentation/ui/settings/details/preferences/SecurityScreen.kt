package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.PasswordChangeRequest
import com.jesus.gymcontrol.domain.viewmodels.password.ChangePasswordViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
	navController: NavController,
) {
	GymTheme {
		SecurityContent(navBottom = navController)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityContent(
	navBottom: NavController,
	viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
	var currentPassword by remember { mutableStateOf("") }
	var newPassword by remember { mutableStateOf("") }
	var confirmPassword by remember { mutableStateOf("") }
	var showCurrentPassword by remember { mutableStateOf(false) }
	var showNewPassword by remember { mutableStateOf(false) }
	var showConfirmPassword by remember { mutableStateOf(false) }
	var passwordError by remember { mutableStateOf(false) }
	var showSuccessDialog by remember { mutableStateOf(false) }
	val isFormValid = currentPassword.isNotBlank()
			&& newPassword.isNotBlank()
			&& confirmPassword.isNotBlank()
			&& newPassword == confirmPassword
	
	val context = LocalContext.current
	
	fun validatePasswords(): Boolean {
		val isValid = newPassword == confirmPassword
		passwordError = !isValid && confirmPassword.isNotEmpty()
		return isValid
	}
	
	LaunchedEffect(viewModel.isSuccess) {
		if (viewModel.isSuccess) {
			showSuccessDialog = true
			delay(1000)
			showSuccessDialog = false
			viewModel.resetState()
			currentPassword = ""
			newPassword = ""
			confirmPassword = ""
		}
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text("Seguridad", color = Color.White, fontWeight = FontWeight.Medium)
				},
				navigationIcon = {
					IconButton(onClick = { navBottom.popBackStack() }) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primary)
			)
		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = 16.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Top
			) {
				
				Image(
					painter = painterResource(id = R.drawable.ic_secutiry),
					contentDescription = "Seguridad",
					modifier = Modifier
						.size(240.dp)
						.padding(top=24.dp)
				)
				
				
				OutlinedTextField(
					value = currentPassword,
					onValueChange = { currentPassword = it },
					label = { Text("Contraseña actual") },
					placeholder = { Text("Ingresa tu contraseña actual") },
					visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
					trailingIcon = {
						IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
							Icon(
								painter = painterResource(id = if (showCurrentPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility),
								contentDescription = null,
								modifier = Modifier.size(22.dp)
							)
						}
					},
					leadingIcon = {
						Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(20.dp))
					},
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					singleLine = true,
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp)
				)
				
				OutlinedTextField(
					value = newPassword,
					onValueChange = {
						newPassword = it
						if (passwordError) validatePasswords()
					},
					label = { Text("Nueva contraseña") },
					placeholder = { Text("Ingresa tu nueva contraseña") },
					visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
					trailingIcon = {
						IconButton(onClick = { showNewPassword = !showNewPassword }) {
							Icon(
								painter = painterResource(id = if (showNewPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility),
								contentDescription = null,
								modifier = Modifier.size(22.dp)
							)
						}
					},
					leadingIcon = {
						Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(20.dp))
					},
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					singleLine = true,
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp)
				)
				
				OutlinedTextField(
					value = confirmPassword,
					onValueChange = {
						confirmPassword = it
						passwordError = it != newPassword
					},
					label = { Text("Confirmar nueva contraseña") },
					placeholder = { Text("Confirma tu nueva contraseña") },
					visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
					isError = passwordError,
					supportingText = {
						if (passwordError) {
							Text("Las contraseñas no coinciden", color = Color.Red)
						}
					},
					trailingIcon = {
						IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
							Icon(
								painter = painterResource(id = if (showConfirmPassword) R.drawable.ic_visibility else R.drawable.ic_no_visibility),
								contentDescription = null,
								modifier = Modifier.size(22.dp)
							)
						}
					},
					leadingIcon = {
						Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(20.dp))
					},
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					singleLine = true,
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp)
				)
				
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 32.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Button(
						onClick = {
							if (validatePasswords()) {
								viewModel.changePassword(
									PasswordChangeRequest(
										currentPassword = currentPassword,
										newPassword = newPassword,
										confirmPassword = confirmPassword
									)
								)
							}
						},
						enabled = isFormValid,
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = if (isFormValid)  colorScheme.primary else Color.Gray
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
						colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
					) {
						Text("Eliminar cuenta", color = Color.White)
					}
				}
			}
			
			// Loader centrado
			if (viewModel.isLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(Color.Black.copy(alpha = 0.3f)),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(color = colorScheme.primary, strokeWidth = 4.dp)
				}
			}
			
			// Alert de éxito
			if (showSuccessDialog) {
				AlertDialog(
					onDismissRequest = { showSuccessDialog = false },
					title = { Text("¡Éxito!") },
					icon = {
						Icon(
							imageVector = Icons.Default.CheckCircle,
							contentDescription = null,
							tint = Color(0xFF4CAF50)
						)
					},
					confirmButton = {
						TextButton(onClick = { showSuccessDialog = false }) {
							Text(
								"Aceptar",
								color = Color.White
							)
						}
					}, containerColor = colorScheme.surface
				)
			}
		}
	}
}