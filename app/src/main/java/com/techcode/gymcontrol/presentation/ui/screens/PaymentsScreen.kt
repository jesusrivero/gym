package com.techcode.gymcontrol.presentation.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
	navBottom: NavController,
) {
	var Description by remember { mutableStateOf("") }
	var Reference by remember { mutableStateOf("") }
	var paymentFrequency by remember { mutableStateOf("") }
	var typepayment by remember { mutableStateOf("") }
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Registro de pagos",
						color = Color.White,
						fontWeight = FontWeight.Bold
					)
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xBAA7D3DC)
				)
			)
		},
		bottomBar = {
			BottomNavigationBar(
				navController = navBottom,
			)
		}
	)
	{ innerPadding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			Column(
				modifier = Modifier.verticalScroll(rememberScrollState())
					.fillMaxSize()
					.padding(16.dp)
			) {
				
				OutlinedTextField(
					value = "",
					onValueChange = {},
					label = { Text("Nombre del cliente") },
					modifier = Modifier
						.fillMaxWidth()
				)
				
				Spacer(modifier = Modifier.padding(8.dp))
				
				ExposedDropdownMenuBox(
					expanded = false,
					onExpandedChange = {}
				) {
					OutlinedTextField(
						value = paymentFrequency,
						onValueChange = {},
						label = { Text("Frecuencia de pago") },
						modifier = Modifier.fillMaxWidth(),
						readOnly = true,
						trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
					)
					
					ExposedDropdownMenu(
						expanded = false,
						onDismissRequest = {}
					) {
						listOf("Semanal", "Quincenal", "Mensual").forEach { frequency ->
							DropdownMenuItem(
								text = { Text(frequency) },
								onClick = {
									paymentFrequency = frequency
								}
							)
						}
					}
				}
				
				
				Spacer(modifier = Modifier.padding(8.dp))
				
				ExposedDropdownMenuBox(
					expanded = false,
					onExpandedChange = {}
				) {
					OutlinedTextField(
						value = typepayment,
						onValueChange = {},
						label = { Text("Tipo de pago") },
						modifier = Modifier.fillMaxWidth(),
						readOnly = true,
						trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
					)
					
					ExposedDropdownMenu(
						expanded = false,
						onDismissRequest = {}
					) {
						listOf("Pago Movil", "Dolares", "Mixto").forEach { frequency ->
							DropdownMenuItem(
								text = { Text(frequency) },
								onClick = {
									typepayment = frequency
								}
							)
						}
					}
				}
				
				
				Spacer(modifier = Modifier.padding(8.dp))
				MontoSection(it = PaddingValues())
				
				Spacer(modifier = Modifier.padding(8.dp))
				
				OutlinedTextField(
					value = Reference,
					onValueChange = { Reference = it },
					label = { Text("Referencia del pago") },
					modifier = Modifier
						.fillMaxWidth()
				)
				Spacer(modifier = Modifier.padding(8.dp))
				
				OutlinedTextField(
					value = Description,
					onValueChange = { Description = it },
					label = { Text("Descripcion") },
					modifier = Modifier
						.fillMaxWidth()
				)
				Spacer(modifier = Modifier.padding(8.dp))
				
				
				
				Button(
					onClick = { },
					colors = ButtonDefaults.buttonColors(containerColor = Color(0xBAA7D3DC)),
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.fillMaxWidth()
						.padding(bottom = 16.dp)
				) {
					Text(text = "Guardar")
				}
				
			}
			
		}
		
	}
}

@Composable
fun MontoSection(it: PaddingValues) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		
		var Montobs by remember { mutableStateOf("") }
		var MontoD by remember { mutableStateOf("") }
		
		Text(
			text = "Monto",
			style = MaterialTheme.typography.titleLarge,
			
			)
		
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			
			OutlinedTextField(
				value = Montobs,
				onValueChange = { Montobs = it },
				label = { Text("Monto en Bs") },
				modifier = Modifier
					.weight(1f)
					.padding(end = 10.dp),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
			)
			
			
			OutlinedTextField(
				value = MontoD,
				onValueChange = { MontoD = it },
				label = { Text("Monto en $") },
				modifier = Modifier.weight(1f),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
			)
		}
		
	}
}


@Preview(showBackground = true)
@Composable
fun PaymentsScreenPreview() {
	PaymentsScreen(
		navBottom = NavController(LocalContext.current),
		
		)
}