package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
	viewModel: UserPreferencesViewModel = hiltViewModel(),
) {
	val context = LocalContext.current
	
	var isEditing by rememberSaveable { mutableStateOf(false) }
	var showDialog by rememberSaveable { mutableStateOf(false) }
	
	// Estados de usuario para los campos del formulario
	var name by rememberSaveable { mutableStateOf("") }
	var phone by rememberSaveable { mutableStateOf("") }
	var age by rememberSaveable { mutableStateOf("") }
	var gender by rememberSaveable { mutableStateOf("") }
	var weight by rememberSaveable { mutableStateOf("") }
	var height by rememberSaveable { mutableStateOf("") }
	
	var isGenderDropdownExpanded by rememberSaveable { mutableStateOf(false) }
	val genderOptions = listOf("Masculino", "Femenino", "Otro")
	
	var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
	
	var profileReloadKey by rememberSaveable { mutableStateOf(0) }
	val isPhoneValid = phone.isBlank() || phone.matches(Regex("^\\+?[1-9]\\d{7,14}$"))
	
	
	
	if (viewModel.isSuccess && !showSuccessDialog) {
		showSuccessDialog = true
	}
	
	LaunchedEffect(showSuccessDialog) {
		if (showSuccessDialog) {
			delay(1000)
			showSuccessDialog = false
			viewModel.resetState()
		}
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
			height = user.height?.toString() ?: ""
		}
	}
	
	LaunchedEffect(viewModel.userProfile) {
		viewModel.userProfile?.let { user ->
			name = user.name
			phone = user.phone
			age = user.age?.toString() ?: ""
			gender = user.gender
			weight = user.weight?.toString() ?: ""
			height = user.height?.toString() ?: ""
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
				
				if (phone.isNotBlank() && !isPhoneValid) {
					Text(
						text = "Número inválido. Usa formato internacional: ej. 58412XXXXXXX",
						color = MaterialTheme.colorScheme.error
					)
				}
				
				
				ExposedDropdownMenuBox(
					expanded = isGenderDropdownExpanded,
					onExpandedChange = {
						if (isEditing) {
							isGenderDropdownExpanded = it
						}
					}
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
						},
					)
					
					if (isEditing) {
						ExposedDropdownMenu(
							expanded = isGenderDropdownExpanded,
							onDismissRequest = { isGenderDropdownExpanded = false },
							modifier = Modifier
								.background(colorScheme.surfaceVariant)
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
									Toast.makeText(context, "El nombre no puede estar vacío", Toast.LENGTH_SHORT)
										.show()
								} else if (!isPhoneValid) {
									Toast.makeText(context, "Número telefónico inválido", Toast.LENGTH_SHORT).show()
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
							Button(
								onClick = {
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
								},
								modifier = Modifier.weight(1f),
								colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
							) {
								Text("Confirmar")
							}
						},
						dismissButton = {
							Button(
								onClick = {
									showDialog = false // 👈 Cierra el diálogo
									isEditing = false
									viewModel.loadUserProfile()
									viewModel.resetState()
								},
								modifier = Modifier.weight(1f),
								colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)) // rojo
							) {
								Text("Cancelar", color = Color.White)
							}
						},
						containerColor = Color.White
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
								tint = Color(0xFF4CAF50)
							)
						},
						containerColor = colorScheme.surface,
						titleContentColor = colorScheme.onSurface,
						textContentColor = colorScheme.onSurface
					)
					
				}
			}
		}
	}
}