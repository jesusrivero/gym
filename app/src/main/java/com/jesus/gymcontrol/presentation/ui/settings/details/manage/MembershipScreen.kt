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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.jesus.gymcontrol.domain.model.Membership
import com.jesus.gymcontrol.domain.viewmodels.MembershipViewModel


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
	
	var showCreateDialog by remember { mutableStateOf(false) }
	var name by remember { mutableStateOf("") }
	var price by remember { mutableStateOf("") }
	var duration by remember { mutableStateOf("") }
	
	var membershipToDelete by remember { mutableStateOf<Membership?>(null) }
	var membershipToEdit by remember { mutableStateOf<Membership?>(null) }
	var membershipToView by remember { mutableStateOf<Membership?>(null) }
	
	LaunchedEffect(Unit) {
		viewModel.loadMembershipsSummary()
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
							painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_back),
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
		}
	) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.padding(horizontal = 20.dp, vertical = 12.dp)
				.fillMaxSize()
		) {
			when {
				isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
					CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
				}
				
				membershipsSummary.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
					Text("No hay membresías registradas.")
				}
				
				else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
								Spacer(Modifier.height(12.dp))
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
									IconButton(onClick = { membershipToDelete = membership }) {
										Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
			
			// Diálogo para eliminar membresía
			membershipToDelete?.let { membership ->
				AlertDialog(
					onDismissRequest = { membershipToDelete = null },
					title = { Text("¿Eliminar membresía?") },
					text = { Text("¿Estás seguro de eliminar la membresía \"${membership.nombre}\"? Esta acción no se puede deshacer.") },
					confirmButton = {
						Button(onClick = {
							viewModel.deleteMembership(membership)
							membershipToDelete = null
						}) {
							Text("Eliminar")
						}
					},
					dismissButton = {
						OutlinedButton(onClick = { membershipToDelete = null }) {
							Text("Cancelar")
						}
					}, 	containerColor = MaterialTheme.colorScheme.surface
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
