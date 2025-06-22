package com.jesus.gymcontrol.presentation.ui.settings.details.manage
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.viewmodels.PromotionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionScreen(
	viewModel: PromotionViewModel = hiltViewModel(),
	navController: NavController,
) {
	val context = LocalContext.current
	val isLoading = viewModel.isLoading
	val promotions = viewModel.promotions
	val errorMessage = viewModel.errorMessage
	
	var showCreateDialog by remember { mutableStateOf(false) }
	var name by remember { mutableStateOf("") }
	var description by remember { mutableStateOf("") }
	var discount by remember { mutableStateOf("") }
	var duration by remember { mutableStateOf("") }
	
	LaunchedEffect(Unit) {
		viewModel.loadPromotions()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text("Promociones", color = MaterialTheme.colorScheme.onPrimary)
				},
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(
							painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = { showCreateDialog = true },
				containerColor = MaterialTheme.colorScheme.primary
			) {
				Icon(Icons.Default.Add, contentDescription = "Nueva promoción")
			}
		}
	) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.padding(16.dp)
				.fillMaxSize()
		) {
			when {
				isLoading -> {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
					}
				}
				promotions.isEmpty() -> {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						Text("No hay promociones registradas")
					}
				}
				else -> {
					LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
						items(promotions) { promo ->
							Card(
								modifier = Modifier.fillMaxWidth(),
								shape = RoundedCornerShape(16.dp),
								elevation = CardDefaults.cardElevation(4.dp),
								colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
							) {
								Column(modifier = Modifier.padding(16.dp)) {
									Text(
										text = promo.nombre,
										style = MaterialTheme.typography.titleMedium,
										color = MaterialTheme.colorScheme.primary
									)
									Text("${promo.porcentajeDescuento}% de descuento")
									Text("${promo.duracionDias} días de duración")
									Spacer(modifier = Modifier.height(8.dp))
									Text(promo.descripcion, style = MaterialTheme.typography.bodySmall)
								}
							}
						}
					}
				}
			}
			
			if (showCreateDialog) {
				AlertDialog(
					onDismissRequest = { showCreateDialog = false },
					confirmButton = {
						Button(onClick = {
							val discountValue = discount.toDoubleOrNull()
							val durationValue = duration.toIntOrNull()
							
							if (name.isBlank() || description.isBlank() || discountValue == null || durationValue == null) {
								Toast.makeText(context, "Complete correctamente los campos", Toast.LENGTH_SHORT).show()
								return@Button
							}
							
							val promotion = Promotion(
								id = "",
								nombre = name.trim(),
								descripcion = description.trim(),
								porcentajeDescuento = discountValue,
								duracionDias = durationValue,
								gimnasioCode = ""
							)
							viewModel.createPromotion(promotion)
							showCreateDialog = false
							name = ""
							description = ""
							discount = ""
							duration = ""
							Toast.makeText(context, "Promoción creada exitosamente", Toast.LENGTH_SHORT).show()
						}) {
							Text("Guardar")
						}
					},
					dismissButton = {
						OutlinedButton(onClick = { showCreateDialog = false }) {
							Text("Cancelar")
						}
					},
					title = { Text("Nueva Promoción") },
					text = {
						Column {
							OutlinedTextField(
								value = name,
								onValueChange = { name = it },
								label = { Text("Nombre") },
								modifier = Modifier.fillMaxWidth()
							)
							OutlinedTextField(
								value = description,
								onValueChange = { description = it },
								label = { Text("Descripción") },
								modifier = Modifier.fillMaxWidth()
							)
							OutlinedTextField(
								value = discount,
								onValueChange = { discount = it },
								label = { Text("Descuento (%)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
							OutlinedTextField(
								value = duration,
								onValueChange = { duration = it },
								label = { Text("Duración (días)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
						}
					},
					shape = RoundedCornerShape(16.dp)
				)
			}
			
			errorMessage?.let {
				LaunchedEffect(it) {
					Toast.makeText(context, it, Toast.LENGTH_LONG).show()
				}
			}
		}
	}
}

