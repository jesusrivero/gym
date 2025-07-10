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
import androidx.compose.runtime.collectAsState
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
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonScreen(
	navController: NavController,
	viewModel: AuthViewModel,
	uid: String
){
	val colorScheme = MaterialTheme.colorScheme
	var showSnackbar by remember { mutableStateOf(false) }
	var snackbarMessage by remember { mutableStateOf("") }
	
	val updateSuccess by viewModel.updateDatesSuccess.collectAsState(initial = null)
	
	if (updateSuccess == true) {
		LaunchedEffect(Unit) {
			snackbarMessage = "Datos actualizados correctamente"
			showSnackbar = true
			delay(2000)
			navController.popBackStack()
		}
	} else if (updateSuccess == false) {
		LaunchedEffect(Unit) {
			snackbarMessage = viewModel.errorMessage ?: "Error al actualizar"
			showSnackbar = true
		}
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
				Snackbar(modifier = Modifier.padding(8.dp)) {
					Text(text = snackbarMessage)
				}
			}
		}
	) { paddingValues ->
		EditPersonContent(
			modifier = Modifier.padding(paddingValues),
			viewModel = viewModel
		)
	}
}
@Composable
fun EditPersonContent(
	modifier: Modifier = Modifier,
	viewModel: AuthViewModel
) {
	var idCard by remember { mutableStateOf("") }
	var age by remember { mutableStateOf("") }
	var phone by remember { mutableStateOf("") }
	var gender by remember { mutableStateOf("") }
	var name by remember { mutableStateOf("") }
	
	
	val colorScheme = MaterialTheme.colorScheme
	
	val formIsValid =
		idCard.isNotBlank() && age.isNotBlank() && phone.isNotBlank() && gender.isNotBlank()
	
	Column(
		modifier = modifier
			.padding(16.dp)
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(
			value = idCard,
			maxLines = 1,
			onValueChange = { idCard = it },
			label = { Text("Cédula") },
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedTextField(
			value = age,
			maxLines = 1,
			onValueChange = { age = it },
			label = { Text("Edad") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedTextField(
			value = phone,
			maxLines = 1,
			onValueChange = { phone = it },
			label = { Text("Teléfono") },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedTextField(
			value = gender,
			maxLines = 1,
			onValueChange = { gender = it },
			label = { Text("Género") },
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Button(
			onClick = {
				val uid = viewModel.currentUid() ?: return@Button
				val gymCode = viewModel.currentGymCode() ?: return@Button
				
				viewModel.updateDatesUser(
					uid = uid,
					idcard = idCard,
					phone = phone,
					name = name,
				)
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


