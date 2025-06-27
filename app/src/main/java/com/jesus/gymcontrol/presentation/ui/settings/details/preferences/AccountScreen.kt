package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.UserUpdate
import com.jesus.gymcontrol.domain.viewmodels.UserPreferencesViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
	navController: NavController,
) {
	GymTheme {
		AccountContent(navBottom = navController)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent(
	navBottom: NavController,
	viewModel: UserPreferencesViewModel = hiltViewModel()
) {
	val context = LocalContext.current
	
	var isEditing by remember { mutableStateOf(false) }
	var showDialog by remember { mutableStateOf(false) }
	
	// Estados de usuario para los campos del formulario
	var name by remember { mutableStateOf("") }
	var phone by remember { mutableStateOf("") }
	var age by remember { mutableStateOf("") }
	var gender by remember { mutableStateOf("") }
	var weight by remember { mutableStateOf("") }
	var height by remember { mutableStateOf("") }
	
	var isGenderDropdownExpanded by remember { mutableStateOf(false) }
	val genderOptions = listOf("Masculino", "Femenino", "Otro")
	
	var showSuccessDialog by remember { mutableStateOf(false) }
	
	var profileReloadKey by remember { mutableStateOf(0) }
	
	
	if (viewModel.isSuccess && !showSuccessDialog) {
		showSuccessDialog = true
	}
	
	LaunchedEffect(Unit) {
		viewModel.loadUserProfile()
	}
	
	LaunchedEffect(profileReloadKey, viewModel.userProfile) {
		viewModel.userProfile?.let { user ->
			name = user.name
			phone = user.phone
			age = user.age?.toString() ?: ""
			gender = user.gender
			weight = user.weight?.toString() ?: ""
			height = user.height?.toString()?:""
		}
	}
	
	LaunchedEffect(viewModel.userProfile) {
		viewModel.userProfile?.let { user ->
			name = user.name
			phone = user.phone
			age = user.age?.toString() ?: ""
			gender = user.gender
			weight = user.weight?.toString() ?: ""
			height = user.height?.toString()?:""
		}
	}
	
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text("Cuenta y Datos", color = Color.White, fontWeight = FontWeight.Medium)
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
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(16.dp))
			
			Column(
				modifier = Modifier.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				OutlinedTextField(
					value = name,
					onValueChange = { name = it },
					label = { Text("Nombre") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true
				)
				
				OutlinedTextField(
					value = phone,
					onValueChange = { phone = it },
					label = { Text("Teléfono") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
				)
				
				ExposedDropdownMenuBox(
					expanded = isGenderDropdownExpanded,
					onExpandedChange = { isGenderDropdownExpanded = it }
				) {
					OutlinedTextField(
						value = gender,
						onValueChange = {},
						readOnly = true,
						enabled = isEditing,
						label = { Text("Género") },
						modifier = Modifier
							.menuAnchor()
							.fillMaxWidth(),
						trailingIcon = {
							ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderDropdownExpanded)
						}
					)
					ExposedDropdownMenu(
						expanded = isGenderDropdownExpanded,
						onDismissRequest = { isGenderDropdownExpanded = false }
					) {
						genderOptions.forEach { option ->
							DropdownMenuItem(
								text = { Text(option) },
								onClick = {
									gender = option
									isGenderDropdownExpanded = false
								}
							)
						}
					}
				}
				
				OutlinedTextField(
					value = age,
					onValueChange = { age = it },
					label = { Text("Edad (años)") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				
				OutlinedTextField(
					value = weight,
					onValueChange = { weight = it },
					label = { Text("Peso (kg)") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				
				OutlinedTextField(
					value = height,
					onValueChange = { height = it },
					label = { Text("Altura (cm)") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				
				Spacer(modifier = Modifier.height(16.dp))
				
				if (!isEditing) {
					Button(
						onClick = { isEditing = true },
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
					) {
						Text("Editar Datos")
					}
				} else {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(16.dp)
					) {
						Button(
							onClick = {
								// Validación simple para evitar que nombre o correo estén vacíos
								if (name.isBlank()) {
									Toast.makeText(context, "Nombre y correo no pueden estar vacíos", Toast.LENGTH_SHORT).show()
								} else {
									showDialog = true
								}
							},
							modifier = Modifier.weight(1f),
							colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
						) {
							Text("Guardar Cambios")
						}
						
						Button(
							onClick = {
								isEditing = false
								viewModel.loadUserProfile()
								profileReloadKey++
								viewModel.resetState()
							},
							modifier = Modifier.weight(1f),
							colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
						) {
							Text("Cancelar")
						}
					}
				}
				
				// Confirmación antes de actualizar
				if (showDialog) {
					AlertDialog(
						onDismissRequest = { showDialog = false },
						title = { Text("¿Confirmar cambios?") },
						text = { Text("Estás por actualizar los datos de tu cuenta. ¿Deseas continuar?") },
						confirmButton = {
							TextButton(onClick = {
								showDialog = false
								viewModel.updateProfile(
									UserUpdate(
										name = name,
										phone = phone.ifBlank { null },
										age = age.toIntOrNull(),
										gender = gender.ifBlank { null },
										weight = weight.toDoubleOrNull(),
										height = height.toDoubleOrNull()
									)
								)
								isEditing = false
							}) {
								Text("Confirmar")
							}
						},
						dismissButton = {
							Button(
								onClick = {
									isEditing = false
									viewModel.loadUserProfile()
									viewModel.resetState()
								},
								modifier = Modifier.weight(1f),
								colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
							) {
								Text("Cancelar")
							}
						}
						
					)
					// Indicadores visuales de estado
					if (viewModel.isLoading) {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.background(Color.Black.copy(alpha = 0.3f)),
							contentAlignment = Alignment.Center
						) {
							CircularProgressIndicator(
								color = colorScheme.primary,
								strokeWidth = 4.dp
							)
						}
					}
				}
				
				
				viewModel.errorMessage?.let {
					Spacer(modifier = Modifier.height(16.dp))
					Text(text = it, color = Color.Red)
				}
				
				if (showSuccessDialog) {
					AlertDialog(
						onDismissRequest = { showSuccessDialog = false },
						title = { Text("¡Éxito!") },
						text = { Text("Información actualizada con éxito.") },
						confirmButton = {},
						icon = {
							Icon(
								imageVector = Icons.Default.CheckCircle,
								contentDescription = null,
								tint = Color(0xFF4CAF50) // verde éxito
							)
						},
						containerColor = MaterialTheme.colorScheme.surface,
						titleContentColor = MaterialTheme.colorScheme.onSurface,
						textContentColor = MaterialTheme.colorScheme.onSurface
					)
					
					// Cierra el dialog después de 2 segundos
					LaunchedEffect(Unit) {
						delay(2000)
						showSuccessDialog = false
						viewModel.resetState() // importante para resetear isSuccess
						}
				}
			}
		}
	}
}