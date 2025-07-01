package com.jesus.gymcontrol.presentation.ui.commons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentFilters(
	selectedPaymentType: String,
	paymentTypeOptions: List<String>,
	onPaymentTypeSelected: (String) -> Unit,
	onClearFilters: () -> Unit,
	searchText: String,
	onSearchTextChanged: (String) -> Unit,
	onAddClick: () -> Unit = {},          // Acción personalizada
	showAddButton: Boolean = true         // Mostrar botón Agregar o no
) {
	var expandedFilter by remember { mutableStateOf(false) }
	
	Column {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp),
			shape = RoundedCornerShape(16.dp),
			color = colorScheme.surface,
			shadowElevation = 4.dp
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				
				Text(
					text = "Tipo de pago",
					style = MaterialTheme.typography.labelMedium,
					color = colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(bottom = 4.dp)
				)
				
				ExposedDropdownMenuBox(
					expanded = expandedFilter,
					onExpandedChange = { expandedFilter = it }
				) {
					OutlinedTextField(
						value = selectedPaymentType,
						onValueChange = {},
						readOnly = true,
						trailingIcon = {
							ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter)
						},
						modifier = Modifier
							.fillMaxWidth()
							.menuAnchor(),
						shape = RoundedCornerShape(12.dp)
					)
					
					ExposedDropdownMenu(
						expanded = expandedFilter,
						onDismissRequest = { expandedFilter = false },
						modifier = Modifier.background(Color.White)
					) {
						paymentTypeOptions.forEach { option ->
							DropdownMenuItem(
								text = { Text(option, modifier = Modifier.fillMaxWidth()) },
								onClick = {
									onPaymentTypeSelected(option)
									expandedFilter = false
								}
							)
						}
					}
				}
				
				Spacer(modifier = Modifier.height(16.dp))
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = if (showAddButton) Arrangement.SpaceBetween else Arrangement.Center
				) {
					Button(
						onClick = onClearFilters,
						modifier = Modifier.weight(1f),
						shape = RoundedCornerShape(12.dp),
						colors = ButtonDefaults.buttonColors(
							containerColor = colorScheme.primary,
							contentColor = colorScheme.onPrimary
						)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Limpiar",
							modifier = Modifier.size(18.dp)
						)
						Spacer(modifier = Modifier.width(4.dp))
						Text("Limpiar")
					}
					
					if (showAddButton) {
						Spacer(modifier = Modifier.width(8.dp))
						
						Button(
							onClick = onAddClick,
							modifier = Modifier.weight(1f),
							shape = RoundedCornerShape(12.dp),
							colors = ButtonDefaults.buttonColors(
								containerColor = colorScheme.primary,
								contentColor = colorScheme.onPrimary
							)
						) {
							Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(18.dp))
							Spacer(modifier = Modifier.width(4.dp))
							Text("Agregar")
							}
						}
				}
				
			}
		}
		
		TextField(
			value = searchText,
			onValueChange = onSearchTextChanged,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp),
			placeholder = { Text("Buscar por nombre o referencia...") },
			leadingIcon = {
				Icon(
					Icons.Default.Search,
					contentDescription = "Buscar",
					tint = colorScheme.primary
				)
			},
			shape = RoundedCornerShape(16.dp),
			colors = TextFieldDefaults.colors(
				focusedContainerColor = colorScheme.surface,
				unfocusedContainerColor = colorScheme.surface,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				focusedTextColor = colorScheme.onSurface,
				unfocusedTextColor = colorScheme.onSurface
			),
			singleLine = true,
			)
	}
}