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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
	viewModel: MembershipViewModel = hiltViewModel(),
	navController: NavController,
) {
	val context = LocalContext.current
	val isLoading = viewModel.isLoading
	val membershipsSummary = viewModel.membershipsSummary
	val errorMessage = viewModel.errorMessage
	val membershipActionMessage by viewModel.membershipActionMessage
	
	var showCreateDialog by remember { mutableStateOf(false) }
	var name by remember { mutableStateOf("") }
	var price by remember { mutableStateOf("") }
	var duration by remember { mutableStateOf("") }
	var membershipToToggle by remember { mutableStateOf<Membership?>(null) }
	var showSuccessDialog by remember { mutableStateOf(false) }
	
	var membershipToEdit by remember { mutableStateOf<Membership?>(null) }
	var membershipToView by remember { mutableStateOf<Membership?>(null) }
	val isStillLoading = isLoading || membershipsSummary.isEmpty()
	
	
	LaunchedEffect(Unit) {
		viewModel.loadMembershipsSummary()
		viewModel.loadMemberships()
	}
	
	// Mostrar snackbar al cambiar estado
	LaunchedEffect(membershipActionMessage) {
		membershipActionMessage?.let {
			showSuccessDialog = true
			delay(1000)
			showSuccessDialog = false
			viewModel.clearMembershipMessage()
		}
	}
	
	if (showSuccessDialog) {
		AlertDialog(
			onDismissRequest = { showSuccessDialog = false },
			title = { Text("¡Éxito!") },
			text = { Text("Información actualizada con éxito.") },
			confirmButton = {}, // sin botón
			icon = {
				Icon(
					imageVector = Icons.Default.CheckCircle,
					contentDescription = null,
					tint = Color(0xFF4CAF50),
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
					Text(
						text = "Control de Membresías",
						style = MaterialTheme.typography.titleLarge,
						color = MaterialTheme.colorScheme.onPrimary
					)
				},
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
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
				Icon(Icons.Default.Add, contentDescription = "Crear Membresía")
			}
		},
	) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.padding(horizontal = 20.dp, vertical = 12.dp)
				.fillMaxSize()
		) {
			if (isStillLoading) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(top = 32.dp), // opcional: evita recortes por la AppBar
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
				}
			} else {
			LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
				items(membershipsSummary) { membershipWithCount ->
				
				val membership = membershipWithCount.membership
						val userCount = membershipWithCount.userCount
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 4.dp),
							shape = RoundedCornerShape(16.dp),
							elevation = CardDefaults.cardElevation(4.dp),
							colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
						) {
							Column(Modifier.padding(16.dp)) {
								Text(
									membership.nombre,
									style = MaterialTheme.typography.titleMedium,
									color = MaterialTheme.colorScheme.primary
								)
								Spacer(Modifier.height(4.dp))
								Text(
									"Usuarios registrados: $userCount",
									style = MaterialTheme.typography.labelSmall,
									color = MaterialTheme.colorScheme.onSurfaceVariant
								)
								Spacer(Modifier.height(6.dp))
								Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
									IconButton(onClick = { membershipToView = membership }) {
										Icon(
											painterResource(id = R.drawable.ic_details),
											contentDescription = "Detalles"
										)
									}
									IconButton(onClick = { membershipToEdit = membership }) {
										Icon(Icons.Default.Edit, contentDescription = "Editar")
									}
									IconButton(onClick = {
										viewModel.toggleMembershipState(membership)
									}) {
										IconButton(onClick = {
											membershipToToggle = membership // En lugar de llamar directamente al ViewModel
										}) {
											Icon(
												imageVector = if (membership.state == "activo") Icons.Default.Visibility else Icons.Default.VisibilityOff,
												contentDescription = if (membership.state == "activo") "Desactivar" else "Activar",
												tint = if (membership.state == "activo") MaterialTheme.colorScheme.primary else Color.Red
											)
										}
										
									}
								}
							}
						}
					}
				}
			}
			// Detalles de membresía (nuevo)
			membershipToView?.let { membership ->
				AlertDialog(
					onDismissRequest = { membershipToView = null },
					title = { Text("Detalles de Membresía") },
					text = {
						Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
							Text("Nombre: ${membership.nombre}")
							Text("Precio: $${membership.precio}")
							Text("Duración: ${membership.duracionDias} días")
							Text("Estado:  ${membership.state}")
						}
					},
					confirmButton = {
						Button(onClick = { membershipToView = null }) {
							Text("Cerrar")
						}
					}, containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			// Diálogo para crear membresía
			if (showCreateDialog) {
				AlertDialog(
					onDismissRequest = { showCreateDialog = false },
					confirmButton = {
						Button(
							onClick = {
								val parsedPrice = price.toDoubleOrNull()
								val parsedDuration = duration.toIntOrNull()
								if (name.isBlank() || parsedPrice == null || parsedDuration == null) {
									Toast.makeText(
										context,
										"Complete correctamente los campos",
										Toast.LENGTH_SHORT
									).show()
									return@Button
								}
								
								val membership = Membership(
									id = "",
									nombre = name.trim(),
									precio = parsedPrice,
									gimnasioCode = "",
									duracionDias = parsedDuration
								)
								
								viewModel.createMembership(membership)
								showCreateDialog = false
								name = ""
								price = ""
								duration = ""
								Toast.makeText(context, "Membresía creada exitosamente", Toast.LENGTH_SHORT)
									.show()
								viewModel.loadMembershipsSummary()
							},
							enabled = !isLoading
						) {
							if (isLoading) {
								CircularProgressIndicator(
									modifier = Modifier.size(16.dp),
									color = MaterialTheme.colorScheme.onPrimary,
									strokeWidth = 2.dp
								)
							} else {
								Text("Guardar")
							}
						}
					},
					dismissButton = {
						OutlinedButton(onClick = {
							showCreateDialog = false
							name = ""
							price = ""
							duration = ""
						}) {
							Text("Cancelar")
						}
					},
					title = { Text("Nueva Membresía") },
					text = {
						Column {
							OutlinedTextField(
								value = name,
								onValueChange = { name = it },
								label = { Text("Nombre") },
								modifier = Modifier.fillMaxWidth()
							)
							Spacer(modifier = Modifier.height(12.dp))
							OutlinedTextField(
								value = price,
								onValueChange = { price = it },
								label = { Text("Precio") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
							Spacer(modifier = Modifier.height(12.dp))
							OutlinedTextField(
								value = duration,
								onValueChange = { duration = it },
								label = { Text("Duración (días)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
						}
					},
					shape = RoundedCornerShape(16.dp),
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			// Diálogo para editar membresía
			membershipToEdit?.let { membership ->
				var editedName by remember { mutableStateOf(membership.nombre) }
				var editedPrice by remember { mutableStateOf(membership.precio.toString()) }
				var editedDuration by remember { mutableStateOf(membership.duracionDias.toString()) }
				
				AlertDialog(
					onDismissRequest = { membershipToEdit = null },
					confirmButton = {
						Button(onClick = {
							val parsedPrice = editedPrice.toDoubleOrNull()
							val parsedDuration = editedDuration.toIntOrNull()
							if (editedName.isBlank() || parsedPrice == null || parsedDuration == null) {
								Toast.makeText(
									context,
									"Complete correctamente los campos",
									Toast.LENGTH_SHORT
								).show()
								return@Button
							}
							val updatedMembership = membership.copy(
								nombre = editedName.trim(),
								precio = parsedPrice,
								duracionDias = parsedDuration
							)
							viewModel.editMembership(updatedMembership)
							membershipToEdit = null
						}) {
							Text("Guardar")
						}
					},
					dismissButton = {
						OutlinedButton(onClick = { membershipToEdit = null }) {
							Text("Cancelar")
						}
					},
					title = { Text("Editar membresía") },
					text = {
						Column {
							OutlinedTextField(
								value = editedName,
								onValueChange = { editedName = it },
								label = { Text("Nombre") },
								modifier = Modifier.fillMaxWidth()
							)
							Spacer(modifier = Modifier.height(12.dp))
							OutlinedTextField(
								value = editedPrice,
								onValueChange = { editedPrice = it },
								label = { Text("Precio") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
							Spacer(modifier = Modifier.height(12.dp))
							OutlinedTextField(
								value = editedDuration,
								onValueChange = { editedDuration = it },
								label = { Text("Duración (días)") },
								keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
								modifier = Modifier.fillMaxWidth()
							)
						}
					},
					shape = RoundedCornerShape(16.dp),
					containerColor = MaterialTheme.colorScheme.surface
				)
			}
			
			membershipToToggle?.let { selected ->
				AlertDialog(
					onDismissRequest = { membershipToToggle = null },
					title = {
						Text(
							text = if (selected.state == "activo") "Desactivar membresía" else "Activar membresía"
						)
					},containerColor = MaterialTheme.colorScheme.surface,
					text = {
						Text(
							text = if (selected.state == "activo") {
								"¿Estás seguro de que deseas desactivar esta membresía? Ya no podras usarla para agregar pagos nuevos."
							} else {
								"¿Estás seguro de que deseas activar esta membresía?"
							}
						)
					},
					confirmButton = {
						TextButton(onClick = {
							viewModel.toggleMembershipState(selected)
							membershipToToggle = null
						}) {
							Text("Confirmar")
						}
					},
					dismissButton = {
						TextButton(onClick = { membershipToToggle = null }) {
							Text("Cancelar")
						}
					}
				)
			}
//			// Diálogo para eliminar membresía
//			membershipToDelete?.let { membership ->
//				AlertDialog(
//					onDismissRequest = { membershipToDelete = null },
//					title = { Text("¿Eliminar membresía?") },
//					text = { Text("¿Estás seguro de eliminar la membresía \"${membership.nombre}\"? Esta acción no se puede deshacer.") },
//					confirmButton = {
//						Button(onClick = {
//							viewModel.deleteMembership(membership)
//							membershipToDelete = null
//						}) {
//							Text("Eliminar")
//						}
//					},
//					dismissButton = {
//						OutlinedButton(onClick = { membershipToDelete = null }) {
//							Text("Cancelar")
//						}
//					}, 	containerColor = MaterialTheme.colorScheme.surface
//				)
//			}
			
			errorMessage?.let {
				LaunchedEffect(it) {
					Toast.makeText(context, it, Toast.LENGTH_LONG).show()
				}
			}
		}
	}
}
