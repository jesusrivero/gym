package com.jesus.gymcontrol.presentation.ui.settings.details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserViewModel





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivateCodeScreen(
	navController: NavController,
	authViewModel: AuthViewModel = hiltViewModel(),
	gymViewModel: GymViewModel = hiltViewModel(),
	userViewModel: UserViewModel = hiltViewModel(),
) {
	
	Scaffold(
//		topBar = {
//			CenterAlignedTopAppBar(
//				title = {
//					Text(
//						text = "Activar Gimnasio",
//						color = colorScheme.onPrimary,
//						fontWeight = FontWeight.Bold
//					)
//				},
//				navigationIcon = {
//					IconButton(onClick = { navController.popBackStack() }) {
//						Icon(
//							painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_back),
//							contentDescription = "Regresar",
//							tint = colorScheme.onPrimary
//						)
//					}
//				},
//				colors = TopAppBarDefaults.topAppBarColors(
//					containerColor = colorScheme.primary
//				)
//			)
//		}
	) { innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					Brush.verticalGradient(
						listOf(
							MaterialTheme.colorScheme.surfaceVariant,
							MaterialTheme.colorScheme.surface
						)
					)
				)
				.padding(innerPadding)
		) {
			ActivateCodeContent(navController, authViewModel, gymViewModel, userViewModel)
		}
	}
}

@Composable
fun ActivateCodeContent(
	navController: NavController,
	authViewModel: AuthViewModel,
	gymViewModel: GymViewModel,
	userViewModel: UserViewModel,
) {
	val colorScheme = MaterialTheme.colorScheme
	var code by remember { mutableStateOf("") }
	
	val context = LocalContext.current
	val isLoading = gymViewModel.isLoading || userViewModel.isLoading
	val isCodeValid = gymViewModel.isCodeValid
	val codeValidationError = gymViewModel.codeValidationError
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(15.dp)
			.padding(top = 10.dp)
			.verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top
	) {
//		Column(
//			modifier = Modifier
//				.fillMaxWidth()
//				.padding(16.dp)
//		){
//			Card(
//				modifier = Modifier.fillMaxWidth(),
//				colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
//			) {
//				Column(
//					modifier = Modifier.padding(16.dp),
//					horizontalAlignment = Alignment.CenterHorizontally
//				) {
//					Text(
//						text = "Activar Gimnasio",
//						style = MaterialTheme.typography.headlineMedium.copy(
//							fontWeight = FontWeight.Bold,
//							color = colorScheme.onPrimaryContainer
//						),
//						textAlign = TextAlign.Center
//					)
//					Text(
//						text = "Ingresa el código de activación proporcionado por tu gimnasio.",
//						style = MaterialTheme.typography.bodyMedium.copy(
//							color = colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
//						),
//						textAlign = TextAlign.Center
//					)
//				}
//			}
//		}
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(8.dp),
			colors = CardDefaults.cardColors(containerColor = colorScheme.background)
		) {
			Column(
				modifier = Modifier.padding(20.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					"Introduce tu código de activación",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold
				)
				
				Spacer(Modifier.height(12.dp))
				
				OutlinedTextField(
					value = code,
					onValueChange = { code = it },
					modifier = Modifier.fillMaxWidth(),
					placeholder = { Text("Código de activación") },
					leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null) },
					singleLine = true
				)
				
				codeValidationError?.let {
					Spacer(Modifier.height(4.dp))
					Text(it, color = Color.Red, style = MaterialTheme.typography.labelSmall)
				}
				
				Spacer(Modifier.height(20.dp))
				
				Button(
					onClick = {
						if (code.isNotBlank()) {
							gymViewModel.validateOwnerCode(code.trim())
						}
					},
					modifier = Modifier.fillMaxWidth(),
					enabled = code.isNotBlank()
				) {
					Text("Validar código")
				}
				
				if (isCodeValid == true) {
					
					Spacer(Modifier.height(25.dp))
					
					Divider()
					
					Spacer(Modifier.height(25.dp))
					
					Text(
						"Datos del gimnasio",
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)
					
					Spacer(Modifier.height(12.dp))
					
					OutlinedTextField(
						value = gymViewModel.name,
						onValueChange = { gymViewModel.name = it },
						modifier = Modifier.fillMaxWidth(),
						placeholder = { Text("Nombre del gimnasio") },
						leadingIcon = { Icon(Icons.Default.FitnessCenter, null) },
						singleLine = true
					)
					
					Spacer(Modifier.height(12.dp))
					
					OutlinedTextField(
						value = gymViewModel.direction,
						onValueChange = { gymViewModel.direction = it },
						modifier = Modifier.fillMaxWidth(),
						placeholder = { Text("Dirección") },
						leadingIcon = { Icon(Icons.Default.LocationOn, null) },
						singleLine = true
					)
					
					Spacer(Modifier.height(12.dp))
					
					OutlinedTextField(
						value = gymViewModel.rif,
						onValueChange = { gymViewModel.rif = it },
						modifier = Modifier.fillMaxWidth(),
						placeholder = { Text("RIF") },
						leadingIcon = { Icon(Icons.Default.Badge, null) },
						singleLine = true
					)
					
					Spacer(Modifier.height(12.dp))
					
					OutlinedTextField(
						value = gymViewModel.phone,
						onValueChange = { gymViewModel.phone = it },
						modifier = Modifier.fillMaxWidth(),
						placeholder = { Text("Teléfono") },
						leadingIcon = { Icon(Icons.Default.Phone, null) },
						keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
						singleLine = true
					)
					
					Spacer(Modifier.height(20.dp))
					
					Button(
						onClick = {
							authViewModel.newDatesUserLogin("dueño", code, navController)
							gymViewModel.code = code
							gymViewModel.createGym()
						},
						modifier = Modifier.fillMaxWidth(),
						enabled = !isLoading
					) {
						if (isLoading) {
							CircularProgressIndicator(
								modifier = Modifier.size(18.dp),
								color = Color.White,
								strokeWidth = 2.dp
							)
							Spacer(modifier = Modifier.width(8.dp))
						}
						Text("Guardar gimnasio")
					}
				}
			}
		}
	}
}


//
//@Preview(showBackground = true)
//@Composable
//fun ActivateCodePreview() {
//    GymTheme {
//        ActivateCodeContent(navController = rememberNavController())
//    }
//}