package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PaymentScreen() {
	var typePayment by remember { mutableStateOf("") }
	var montoBs by remember { mutableStateOf("") }
	var montoDolares by remember { mutableStateOf("") }
	
	Column() {

		
		TypePaymentSection(
			selectedType = typePayment,
			onTypeSelected = { newValue -> typePayment = newValue }
		)
		
		
	
		Column(modifier = Modifier.fillMaxWidth()) {
			
	
			
			if (typePayment == "Mixto") {
			
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					OutlinedTextField(
						value = montoBs,
						onValueChange = { montoBs = it },
						label = { Text("Monto en Bs") },
						modifier = Modifier.weight(1f),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
					)
					Spacer(modifier = Modifier.width(8.dp))
					OutlinedTextField(
						value = montoDolares,
						onValueChange = { montoDolares = it },
						label = { Text("Monto en $") },
						modifier = Modifier.weight(1f),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
					)
				}
			} else {
				
			
				Column {
					if (typePayment == "Pago Movil") {
						OutlinedTextField(
							value = montoBs,
							onValueChange = { montoBs = it },
							label = { Text("Monto en Bs") },
							modifier = Modifier.fillMaxWidth(),
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
						)
					}
					Spacer(modifier = Modifier.padding(top=8.dp))
					if (typePayment == "Dolares") {
						OutlinedTextField(
							value = montoDolares,
							onValueChange = { montoDolares = it },
							label = { Text("Monto en $") },
							modifier = Modifier.fillMaxWidth(),
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
						)
					}
				}
			}
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypePaymentSection(
	selectedType: String,
	onTypeSelected: (String) -> Unit,
) {
	var expanded by remember { mutableStateOf(false) }
	
	ExposedDropdownMenuBox(
		expanded = expanded,
		onExpandedChange = { expanded = it }
	) {
		OutlinedTextField(
			value = selectedType,
			onValueChange = {},
			label = { Text("Tipo de pago") },
			modifier = Modifier
				.fillMaxWidth()
				.menuAnchor(),
			readOnly = true,
			trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
		)
		
		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			listOf("Pago Movil", "Dolares", "Mixto").forEach { paymentType ->
				DropdownMenuItem(
					text = { Text(paymentType) },
					onClick = {
						onTypeSelected(paymentType)
						expanded = false
					}
				)
			}
		}
	}
}