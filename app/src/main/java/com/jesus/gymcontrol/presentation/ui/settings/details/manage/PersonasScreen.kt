package com.jesus.gymcontrol.presentation.ui.settings.details.manage


import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.WhatsAppButton
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserListViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.ui.commons.PaymentFilters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonsScreen(
	navBottom: NavController,
	userListViewModel: UserListViewModel = hiltViewModel(),
	authViewModel: AuthViewModel = hiltViewModel(),
	navPag: (String, String) -> Unit
) {
	LaunchedEffect(Unit) {
		userListViewModel.loadUsers()
	}
	
	var dialogMode by rememberSaveable { mutableStateOf<DialogMode>(DialogMode.None) }
	var editableUser by remember { mutableStateOf<ListUser?>(null) }
	var searchText by rememberSaveable { mutableStateOf("") }
	var selectedState by rememberSaveable { mutableStateOf("Todos") }
	
	val configuration = LocalConfiguration.current
	val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
	
	if (editableUser != null && dialogMode != DialogMode.None) {
		if (dialogMode == DialogMode.Edit) {
			EditUserDialog(
				user = editableUser!!,
				onDismiss = {
					dialogMode = DialogMode.None
					editableUser = null
				},
				onSave = { updatedUser ->
					authViewModel.updateDatesUser(
						uid = updatedUser.id,
						idcard = updatedUser.idcard,
						phone = updatedUser.phone,
						name = updatedUser.name,
						lastname = updatedUser.lastname
					)
					userListViewModel.loadUsers()
					dialogMode = DialogMode.None
					editableUser = null
				}
			)
		} else if (dialogMode == DialogMode.View) {
			ViewUserDialog(
				user = editableUser!!,
				onDismiss = {
					dialogMode = DialogMode.None
					editableUser = null
				}
			)
		}
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Listado de personas",
						color = MaterialTheme.colorScheme.onPrimary,
						fontWeight = FontWeight.Bold,
					)
				},
				navigationIcon = {
					IconButton(onClick = { navBottom.popBackStack() }) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = "Regresar",
							tint = Color.White
						)
					}
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		}
	) { innerPadding ->
		val users = userListViewModel.listUsers
		val isLoading = userListViewModel.isLoading
		
		val filteredList = users.filter { user ->
			val matchesSearch = searchText.isBlank() ||
					user.name.contains(searchText, ignoreCase = true) ||
					user.lastname.contains(searchText, ignoreCase = true) ||
					user.email.contains(searchText, ignoreCase = true) ||
					user.idcard.contains(searchText, ignoreCase = true) ||
					user.phone.contains(searchText, ignoreCase = true)
			
			val matchesState = when (selectedState) {
				"Todos" -> true
				"Activos" -> user.state.equals("activo", ignoreCase = true)
				"Inactivos" -> user.state.equals("inactivo", ignoreCase = true)
				"Próximos a pagar" -> user.state.equals("pendiente", ignoreCase = true)
				else -> true
			}
			
			matchesSearch && matchesState
		}
		
		val contentModifier = Modifier
			.fillMaxSize()
			.padding(innerPadding)
			.padding(horizontal = if (isLandscape) 16.dp else 0.dp)
		
		Column(modifier = contentModifier) {
			PaymentFilters(
				selectedPaymentType = selectedState,
				paymentTypeOptions = listOf("Todos", "Activos", "Inactivos", "Próximos a pagar"),
				onPaymentTypeSelected = { selectedState = it },
				onClearFilters = {
					selectedState = "Todos"
					searchText = ""
				},
				searchText = searchText,
				onSearchTextChanged = { searchText = it },
				onAddClick = { navBottom.navigate(AppRoutes.RegPersonScreen) },
				showAddButton = true
			)
			
			if (isLoading) {
				Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					CircularProgressIndicator()
				}
			} else if (filteredList.isEmpty()) {
				Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					Text("No hay resultados")
				}
			} else {
				LazyColumn(
					modifier = Modifier.fillMaxSize(),
				) {
					items(filteredList) { user ->
						PersonCard(
							user = user,
							onEdit = {
								editableUser = user
								dialogMode = DialogMode.Edit
							},
							navPag = navPag,
							onViewDetails = {
								editableUser = user
								dialogMode = DialogMode.View
							}
						)
					}
				}
			}
		}
	}
}

enum class DialogMode {
	None, View, Edit
}


@Composable
fun PersonCard(
	user: ListUser,
	onEdit: () -> Unit,
	navPag: (String, String) -> Unit,
	onViewDetails: () -> Unit,
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 6.dp, vertical = 2.dp), // 👈 padding externo
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(),
		colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 10.dp, vertical = 10.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column(
				modifier = Modifier.weight(1f)
			) {
				// 👇 nombre completo
				Text(
					text = "${user.name} ${user.lastname}".trim(),
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold
				)
				Text(
					text = (user.state ?: "Desconocido").replaceFirstChar { it.uppercase() },
					style = MaterialTheme.typography.bodySmall,
					color = estadoColor(user.state),
				)
			}
			
			Row(
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (user.state.equals("pendiente", ignoreCase = true)) {
					WhatsAppButton(
						phoneNumber = user.phone,
						message = "Hola ${user.name} ${user.lastname}, tu membresía está próxima a vencer."
					)
				}
				
				IconButton(onClick = { navPag(user.id, "${user.name} ${user.lastname}".trim()) }) {
					Icon(
						Icons.Default.Payment,
						contentDescription = "Pagar",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				
				IconButton(onClick = onEdit) {
					Icon(
						Icons.Default.Edit,
						contentDescription = "Editar",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				
				IconButton(onClick = onViewDetails) {
					Icon(
						painter = painterResource(id = R.drawable.ic_details),
						contentDescription = "Detalles",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
		}
	}
}



@Composable
fun EditUserDialog(
	user: ListUser,
	onDismiss: () -> Unit,
	onSave: (ListUser) -> Unit
) {
	var name by remember { mutableStateOf(user.name) }
	var lastname by remember { mutableStateOf(user.lastname) }
	var idcard by remember { mutableStateOf(user.idcard) }
	var phone by remember { mutableStateOf(user.phone) }
	val onlyLettersRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*\$")
	var nameError by remember { mutableStateOf<String?>(null) }
	var lastnameError by remember { mutableStateOf<String?>(null) }
	var idcardError by remember { mutableStateOf<String?>(null) }
	var phoneError by remember { mutableStateOf<String?>(null) }
	
	AlertDialog(
		onDismissRequest = onDismiss,
		containerColor = MaterialTheme.colorScheme.surface,
		title = {
			Text("Editar usuario", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
		},
		text = {
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				OutlinedTextField(
					value = name,
					onValueChange = {
						if (it.length <= 35 && it.matches(onlyLettersRegex)) name = it
					},
					label = { Text("Nombre") },
					maxLines = 1,
					isError = nameError != null,
					supportingText = {
						if (nameError != null) Text(nameError!!, color = MaterialTheme.colorScheme.error)
					}
				)
				
				OutlinedTextField(
					value = lastname,
					onValueChange = {
						if (it.length <= 35 && it.matches(onlyLettersRegex)) lastname = it
					},
					label = { Text("Apellido") },
					maxLines = 1,
					isError = lastnameError != null,
					supportingText = {
						if (lastnameError != null) Text(lastnameError!!, color = MaterialTheme.colorScheme.error)
					}
				)
				OutlinedTextField(
					value = idcard,
					onValueChange = {
						if (it.length <= 9) idcard = it.filter { c -> c.isDigit() }
					},
					label = { Text("Cédula") },
					maxLines = 1,
					isError = idcardError != null,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					supportingText = {
						if (idcardError != null) Text(idcardError!!, color = MaterialTheme.colorScheme.error)
					}
				)
				OutlinedTextField(
					value = phone,
					onValueChange = {
						if (it.length <= 15) phone = it.filter { c -> c.isDigit() }
					},
					label = { Text("Teléfono") },
					maxLines = 1,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					isError = phoneError != null,
					supportingText = {
						if (phoneError != null) Text(phoneError!!, color = MaterialTheme.colorScheme.error)
					}
				)
			}
		},
		confirmButton = {
			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier.fillMaxWidth()
			) {
				Button(
					onClick = onDismiss,
					modifier = Modifier.weight(1f)
				) {
					Text("Cerrar")
				}
				
				Button(
					onClick = {
						var isValid = true
						
						if (name.isBlank()) {
							nameError = "El nombre no puede estar vacío"
							isValid = false
						} else {
							nameError = null
						}
						
						if (idcard.length !in 7..9) {
							idcardError = "La cédula debe tener entre 7 y 9 dígitos"
							isValid = false
						} else {
							idcardError = null
						}
						
						if (phone.length !in 10..15) {
							phoneError = "El teléfono debe tener entre 10 y 15 dígitos"
							isValid = false
						} else {
							phoneError = null
						}
						
						if (isValid) {
							onSave(
								user.copy(
									name = name,
									lastname = lastname,
									idcard = idcard,
									phone = phone
								)
							)
						}
					},
					modifier = Modifier.weight(1f)
				) {
					Text("Guardar")
				}
			}
		}
	)
}

@Composable
fun ViewUserDialog(
	user: ListUser,
	onDismiss: () -> Unit
) {
	val formattedDate = user.date.takeIf { it != null && it > 0L }?.let {
		SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
	} ?: "No disponible"
	
	AlertDialog(
		onDismissRequest = onDismiss,
		containerColor = MaterialTheme.colorScheme.surface,
		title = {
			Text("Información del usuario", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
		},
		text = {
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Text("Nombre: ${user.name}")
				Text("Apellido: ${user.lastname}")
				Text("Rol: ${user.rol}")
				Text("Email: ${user.email}")
				Text("Cédula: ${user.idcard}")
				Text("Estado: ${user.state}")
				Text("Teléfono: ${user.phone}")
				Text("Fecha de registro: $formattedDate")
			}
		},
		confirmButton = {
			Button(
				onClick = onDismiss,
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Cerrar")
			}
		}
	)
}



@Composable
fun estadoColor(estado: String?): Color {
	return when (estado?.lowercase()) {
		"activo" -> Color(0xFF2E7D32)        // Verde
		"inactivo" -> Color.Red              // Rojo
		"pendiente" -> Color(0xFFF9A825)    // Amarillo
		else -> MaterialTheme.colorScheme.onSurface
	}
}

@Composable
fun DetailRow(label: String, value: String) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = label,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}