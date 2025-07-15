package com.jesus.gymcontrol.presentation.ui.settings.details.manage

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Promotion
import com.jesus.gymcontrol.domain.viewmodels.PromotionViewModel
import com.jesus.gymcontrol.presentation.ui.commons.PromotionFilter
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
	var promotionToEdit by remember { mutableStateOf<Promotion?>(null) }
	var promotionToDelete by remember { mutableStateOf<Promotion?>(null) }
	var promotionToShow by remember { mutableStateOf<Promotion?>(null) }
	var promotionToggle by remember { mutableStateOf<Promotion?>(null) }
	var showResultDialog by remember { mutableStateOf(false) }
	val promotionActionMessage by viewModel.promotionActionMessage
	val isActionSuccess by viewModel.isActionSuccess.collectAsState()
	var showDiscountError by remember { mutableStateOf(false) }
	var showEditDiscountError by remember { mutableStateOf(false) }
	var filter by remember { mutableStateOf(PromotionFilter.ACTIVE) }
	
	
	
	LaunchedEffect(Unit) {
		viewModel.loadPromotions()
		viewModel.loadUserCountByPromotion()
	}
	
	LaunchedEffect(promotionActionMessage) {
		promotionActionMessage?.let {
			showResultDialog = true
			delay(1000)
			showResultDialog = false
			viewModel.clearPromotionMessage()
		}
	}
	LaunchedEffect(errorMessage) {
		errorMessage?.let {
			Toast.makeText(context, it, Toast.LENGTH_LONG).show()
			viewModel.clearError()
		}
	}
	
	
	if (showResultDialog && promotionActionMessage != null && isActionSuccess != null) {
		val isSuccess = isActionSuccess == true
		AlertDialog(
			onDismissRequest = { showResultDialog = false },
			title = { Text(if (isSuccess) "¡Éxito!" else "Error") },
			text = { Text(promotionActionMessage ?: "") },
			confirmButton = {},
			icon = {
				Icon(
					imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
					contentDescription = null,
					tint = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFD32F2F),
					modifier = Modifier.size(48.dp)
				)
			},
			containerColor = MaterialTheme.colorScheme.surface,
			titleContentColor = MaterialTheme.colorScheme.onSurface,
			textContentColor = MaterialTheme.colorScheme.onSurface
		)
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
		},
		
		) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.padding(horizontal = 16.dp, vertical = 8.dp)
				.fillMaxSize()
		) {
			
			promotionToggle?.let { selected ->
				AlertDialog(
					onDismissRequest = { promotionToggle = null },
					title = {
						Text(
							text = if (selected.activo) "Desactivar promoción" else "Activar promoción"
						)
					},
					text = {
						Text(
							text = if (selected.activo) {
								"¿Estás seguro de que deseas desactivar esta promoción?"
							} else {
								"¿Estás seguro de que deseas activar esta promoción?"
							}
						)
					},
					confirmButton = {
						TextButton(onClick = {
							viewModel.togglePromotionState(selected)
							promotionToggle = null
						}) {
							Text("Confirmar")
						}
					},
					dismissButton = {
						TextButton(onClick = {
							promotionToggle = null
						}) {
							Text("Cancelar")
						}
					},
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			promotionToDelete?.let { selected ->
				AlertDialog(
					onDismissRequest = { promotionToDelete = null },
					title = {
						Text("Eliminar promoción")
					},
					text = {
						Text("¿Estás seguro de que deseas eliminar la promoción \"${selected.nombre}\"? Esta acción no se puede deshacer.")
					},
					confirmButton = {
						TextButton(onClick = {
							viewModel.deletePromotion(selected)
							promotionToDelete = null
						}) {
							Text("Eliminar", color = Color.Red)
						}
					},
					dismissButton = {
						TextButton(onClick = {
							promotionToDelete = null
						}) {
							Text("Cancelar")
						}
					},
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End
			) {
				TextButton(
					onClick = { filter = PromotionFilter.ACTIVE },
					colors = ButtonDefaults.textButtonColors(
						contentColor = if (filter == PromotionFilter.ACTIVE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
					)
				) {
					Text("Activas")
				}
				TextButton(
					onClick = { filter = PromotionFilter.INACTIVE },
					colors = ButtonDefaults.textButtonColors(
						contentColor = if (filter == PromotionFilter.INACTIVE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
					)
				) {
					Text("Inactivas")
				}
			}
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
					val filteredPromotions = when (filter) {
						PromotionFilter.ACTIVE -> promotions.filter { it.activo }
						PromotionFilter.INACTIVE -> promotions.filter { !it.activo }
					}
					LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
						items(filteredPromotions) { promo ->
							Card(
								modifier = Modifier
									.fillMaxWidth()
									.padding(vertical = 2.dp),
								shape = RoundedCornerShape(16.dp),
								elevation = CardDefaults.cardElevation(4.dp),
								colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
							) {
								Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
									
									// Fila con nombre + botones
									Row(
										modifier = Modifier.fillMaxWidth(),
										verticalAlignment = Alignment.CenterVertically,
										horizontalArrangement = Arrangement.SpaceBetween
									) {
										Text(
											text = promo.nombre,
											style = MaterialTheme.typography.titleMedium,
											color = MaterialTheme.colorScheme.primary,
											modifier = Modifier.weight(1f)
										)
										
										Row {
											
											IconButton(onClick = { promotionToEdit = promo }) {
												Icon(Icons.Default.Edit, contentDescription = "Editar promoción")
											}
											IconButton(onClick = { promotionToggle = promo }) {
												Icon(
													imageVector = if (promo.activo) Icons.Default.VisibilityOff else Icons.Default.Visibility,
													contentDescription = if (promo.activo) "Desactivar" else "Activar",
													tint = if (promo.activo) Color.Red  else MaterialTheme.colorScheme.primary
												)
											}
											IconButton(onClick = { promotionToShow = promo }) {
												Icon(
													painterResource(id = R.drawable.ic_details),
													contentDescription = "Detalles"
												)
											}
											IconButton(onClick = { promotionToDelete = promo }) {
												Icon(
													painterResource(id = R.drawable.ic_delete),
													contentDescription = "Eliminar promoción",
												)
											}
											
										}
									}
									
									Spacer(modifier = Modifier.height(4.dp))
									
									// Descripción debajo
									Text(
										"Usuarios: ${promo.cantidadUsuarios}",
										style = MaterialTheme.typography.bodySmall,
										color = MaterialTheme.colorScheme.onSurfaceVariant
									)
								}
							}
						}
					}
				}
			}
			promotionToShow?.let { promo ->
				AlertDialog(
					onDismissRequest = { promotionToShow = null },
					title = { Text("Detalles de promoción") },
					text = {
						Column {
							Text("Nombre: ${promo.nombre}")
							Spacer(modifier = Modifier.height(4.dp))
							Text("Descripción: ${promo.descripcion}")
							Spacer(modifier = Modifier.height(4.dp))
							Text("Duración: ${promo.duracionDias} días")
							Spacer(modifier = Modifier.height(4.dp))
							Text("Descuento: ${promo.porcentajeDescuento}%")
							Spacer(modifier = Modifier.height(4.dp))
							Text("Estado: ${if (promo.activo) "Activa" else "Inactiva"}")
							Spacer(modifier = Modifier.height(4.dp))
							Text(
								"Fecha de creación: ${
									SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
										Date(promo.fechaCreacion)
									)
								}"
							)
							Text(
								"Fecha de vencimiento: ${
									SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
										Date(promo.fechaVencimiento)
									)
								}"
							)
							
						}
					},
					confirmButton = {
						Button(onClick = { promotionToShow = null }) {
							Text("Cerrar")
						}
					},
					shape = RoundedCornerShape(16.dp),
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			if (showCreateDialog) {
				AlertDialog(
					onDismissRequest = { showCreateDialog = false },
					confirmButton = {
						Button(onClick = {
							val discountValue = discount.toDoubleOrNull()
							val durationValue = duration.toIntOrNull()
							
							if (name.isBlank() || description.isBlank() || discountValue == null || durationValue == null) {
								Toast.makeText(context, "Complete correctamente los campos", Toast.LENGTH_SHORT)
									.show()
								return@Button
							}
							
							if (discountValue > 100.0) {
								Toast.makeText(
									context,
									"El descuento no puede ser mayor al 100%",
									Toast.LENGTH_SHORT
								).show()
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
								modifier = Modifier.fillMaxWidth(),
								maxLines = 1
							)
							OutlinedTextField(
								value = description,
								onValueChange = { description = it },
								label = { Text("Descripción") },
								modifier = Modifier.fillMaxWidth(),
								maxLines = 1
							)
							OutlinedTextField(
								value = discount,
								onValueChange = {
									val value = it.toDoubleOrNull()
									if (value == null || value <= 100.0) {
										discount = it
										showDiscountError = false
									} else {
										showDiscountError = true
									}
								},
								label = { Text("Descuento (%)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth(),
								maxLines = 1
							)
							
							if (showDiscountError) {
								Text(
									text = "El descuento no puede ser mayor a 100%",
									color = Color.Red,
									style = MaterialTheme.typography.labelSmall,
									modifier = Modifier.padding(top = 4.dp)
								)
							}
							OutlinedTextField(
								value = duration,
								onValueChange = { duration = it },
								label = { Text("Duración (días)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth(),
								maxLines = 1
							)
						}
					},
					shape = RoundedCornerShape(16.dp),
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			promotionToEdit?.let { promo ->
				var editedName by remember { mutableStateOf(promo.nombre) }
				var editedDescription by remember { mutableStateOf(promo.descripcion) }
				var editedDiscount by remember { mutableStateOf(promo.porcentajeDescuento.toString()) }
				var editedDuration by remember { mutableStateOf(promo.duracionDias.toString()) }
				var canEditDuration by remember { mutableStateOf(false) }
				var showEditDiscountError by remember { mutableStateOf(false) }
				
				val editableFully = promo.cantidadUsuarios == 0
				
				AlertDialog(
					onDismissRequest = { promotionToEdit = null },
					confirmButton = {
						Button(onClick = {
							val discountVal = editedDiscount.toDoubleOrNull()
							val durationVal = editedDuration.toIntOrNull()
							
							// Validaciones
							if (!editableFully && durationVal == null) {
								Toast.makeText(context, "Complete correctamente los campos", Toast.LENGTH_SHORT).show()
								return@Button
							}
							
							if (editableFully && (editedName.isBlank() || editedDescription.isBlank() || discountVal == null || durationVal == null)) {
								Toast.makeText(context, "Complete correctamente los campos", Toast.LENGTH_SHORT).show()
								return@Button
							}
							
							if (discountVal != null && discountVal > 100.0) {
								Toast.makeText(context, "El descuento no puede ser mayor al 100%", Toast.LENGTH_SHORT).show()
								return@Button
							}
							
							val vencida = (promo.fechaVencimiento ?: 0L) < System.currentTimeMillis()
							
							val updatedPromo = promo.copy(
								nombre = editedName.trim(),
								descripcion = editedDescription.trim(),
								porcentajeDescuento = discountVal ?: promo.porcentajeDescuento,
								duracionDias = durationVal ?: promo.duracionDias
							)
							
							viewModel.updatePromotion(updatedPromo, forceRecalculate = vencida || canEditDuration)
							promotionToEdit = null
						}) {
							Text("Guardar")
						}
					},
					dismissButton = {
						OutlinedButton(onClick = { promotionToEdit = null }) {
							Text("Cancelar")
						}
					},
					title = {
						val vencida = (promo.fechaVencimiento ?: 0L) < System.currentTimeMillis()
						val textColor =
							if (vencida) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
						Text(
							text = if (vencida) "Editar Promoción (VENCIDA)" else "Editar Promoción",
							color = textColor
						)
					},
					text = {
						val vencida = (promo.fechaVencimiento ?: 0L) < System.currentTimeMillis()
						val textColor =
							if (vencida) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
						
						Column {
							if (!editableFully && !vencida) { // <-- Aquí la condición para ocultar cuando está vencida
								Text(
									text = "ℹ️ Esta promoción tiene ${promo.cantidadUsuarios} uso(s) registrado(s). Solo puedes actualizar la duración.",
									color = MaterialTheme.colorScheme.onSurfaceVariant,
									style = MaterialTheme.typography.bodySmall,
									modifier = Modifier.padding(bottom = 8.dp)
								)
							}
							
							
							if (editableFully) {
								OutlinedTextField(
									value = editedName,
									onValueChange = { editedName = it },
									label = { Text("Nombre", color = textColor) },
									modifier = Modifier.fillMaxWidth(),
									maxLines = 1
								)
								OutlinedTextField(
									value = editedDescription,
									onValueChange = { editedDescription = it },
									label = { Text("Descripción", color = textColor) },
									modifier = Modifier.fillMaxWidth(),
									maxLines = 1
								)
								OutlinedTextField(
									value = editedDiscount,
									onValueChange = {
										val value = it.toDoubleOrNull()
										if (value == null || value <= 100.0) {
											editedDiscount = it
											showEditDiscountError = false
										} else {
											showEditDiscountError = true
										}
									},
									label = { Text("Descuento (%)", color = textColor) },
									keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
									modifier = Modifier.fillMaxWidth(),
									maxLines = 1
								)
								
								if (showEditDiscountError && !vencida) {
									Text(
										text = "El descuento no puede ser mayor al 100%",
										color = Color.Red,
										style = MaterialTheme.typography.labelSmall,
										modifier = Modifier.padding(top = 4.dp),
										maxLines = 1
									)
								}
							}
							
							Row(
								verticalAlignment = Alignment.CenterVertically,
								modifier = Modifier.padding(top = 8.dp)
							) {
								OutlinedTextField(
									value = editedDuration,
									onValueChange = { editedDuration = it },
									label = { Text("Duración (días)", color = textColor) },
									keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
									modifier = Modifier.weight(1f),
									maxLines = 1
								)
								IconButton(
									onClick = { canEditDuration = !canEditDuration }
								) {
									Icon(
										imageVector = Icons.Default.Edit,
										contentDescription = "Extender duración",
										tint = if (canEditDuration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
									)
								}
							}
						}
					},
					shape = RoundedCornerShape(16.dp),
					containerColor = if ((promo.fechaVencimiento ?: 0L) < System.currentTimeMillis())
						MaterialTheme.colorScheme.errorContainer
					else
						MaterialTheme.colorScheme.surface
				)
			}
			
			
			
			// Diálogo para eliminar promoción
			promotionToDelete?.let { promo ->
				AlertDialog(
					onDismissRequest = { promotionToDelete = null },
					title = { Text("¿Eliminar promoción?") },
					text = { Text("¿Estás seguro de eliminar la promoción \"${promo.nombre}\"? Esta acción no se puede deshacer.") },
					confirmButton = {
						Button(onClick = {
							viewModel.deletePromotion(promo)
							promotionToDelete = null
						}) {
							Text("Eliminar")
						}
					},
					dismissButton = {
						OutlinedButton(onClick = { promotionToDelete = null }) {
							Text("Cancelar")
						}
					}, containerColor = MaterialTheme.colorScheme.surface
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

