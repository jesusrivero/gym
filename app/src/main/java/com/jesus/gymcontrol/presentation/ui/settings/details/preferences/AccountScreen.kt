package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.viewmodels.AdminViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
	navController: NavController,
) {
	GymTheme {
		AccountContent(
			navBottom = navController
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent(
	navBottom: NavController,
	viewModel: AdminViewModel = hiltViewModel()
) {
	var isEditing by remember { mutableStateOf(false) }
	var name by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var phone by remember { mutableStateOf("") }
	var age by remember { mutableStateOf("") }
	var gender by remember { mutableStateOf("") }

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Cuenta y Datos",
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
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 16.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = "Nombre",
					style = MaterialTheme.typography.labelLarge
				)

				OutlinedTextField(
					value = name,
					onValueChange = { name = it },
					label = { Text("Nombre") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true
				)

				Text(
					text = "Correo",
					style = MaterialTheme.typography.labelLarge
				)

				OutlinedTextField(
					value = email,
					onValueChange = { email = it },
					label = { Text("Correo") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
				)

				Text(
					text = "Teléfono",
					style = MaterialTheme.typography.labelLarge
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
				
				Text(
					text = "Genero",
					style = MaterialTheme.typography.labelLarge
				)
				
				OutlinedTextField(
					value = gender,
					onValueChange = { gender = it },
					label = { Text("Genero") },
					modifier = Modifier.fillMaxWidth(),
					enabled = isEditing,
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
				)
				
				Text(
					text = "Edad",
					style = MaterialTheme.typography.labelLarge
				)
				
				OutlinedTextField(
					value = age,
					onValueChange = {  age = it },
					label = { Text("Edad") },
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
						colors = ButtonDefaults.buttonColors(
							containerColor = colorScheme.primary
						)
					) {
						Text("Editar Datos")
					}
				} else {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(16.dp)
					) {
						Button(
							onClick = { isEditing = false },
							modifier = Modifier.weight(1f),
							colors = ButtonDefaults.buttonColors(
								containerColor =colorScheme.primary
							)
						) {
							Text("Guardar Cambios")
						}

						Button(
							onClick = {
								isEditing = false
								name = ""
								email = ""
								phone = ""
							},
							modifier = Modifier.weight(1f),
							colors = ButtonDefaults.buttonColors(
								containerColor = colorScheme.primary
							)
						) {
							Text("Cancelar" )
						}
					}
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
	GymTheme {
		AccountScreen(navController = rememberNavController())
	}
}
