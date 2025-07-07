package com.jesus.gymcontrol.presentation.ui.settings.details.manage


import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.helpers.WhatsAppButton
import com.jesus.gymcontrol.domain.model.ListUser
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
	viewModel: UserListViewModel = hiltViewModel(),
	navEdit: (String) -> Unit,
	navPag: (String) -> Unit,
) {
	LaunchedEffect(Unit) {
		viewModel.loadUsers()
	}
	
	var showUserDialog by rememberSaveable { mutableStateOf(false) }
	var selectedUser by rememberSaveable { mutableStateOf<ListUser?>(null) }
	var searchText by rememberSaveable { mutableStateOf("") }
	var selectedState by rememberSaveable { mutableStateOf("Todos") }
	
	val configuration = LocalConfiguration.current
	val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
	
	if (showUserDialog && selectedUser != null) {
		val formattedDate = selectedUser?.date?.takeIf { it > 0L }?.let {
			SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
		} ?: "No disponible"
		
		AlertDialog(
			onDismissRequest = {
				showUserDialog = false
				selectedUser = null
			},
			title = {
				Text(
					"Información del usuario",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center
				)
			},
			text = {
				Column {
					DetailRow("Nombre:", selectedUser?.name ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Rol:", selectedUser?.rol ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Email:", selectedUser?.email ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Cédula:", selectedUser?.idcard ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Estado:", selectedUser?.state ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Teléfono:", selectedUser?.phone ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Fecha de registro:", formattedDate)
				}
			},
			containerColor = MaterialTheme.colorScheme.surface,
			confirmButton = {
				Button(
					onClick = {
						showUserDialog = false
						selectedUser = null
					},
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primary,
						contentColor = MaterialTheme.colorScheme.onPrimary
					),
				) {
					Text("Cerrar")
				}
			}
		)
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
		val users = viewModel.listUsers
		val isLoading = viewModel.isLoading
		
		val filteredList = users.filter { user ->
			val matchesSearch = searchText.isBlank() ||
					user.name.contains(searchText, ignoreCase = true) ||
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
		
		if (isLandscape) {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.padding(horizontal = 16.dp),
				verticalArrangement = Arrangement.spacedBy(12.dp)
			) {
				item {
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
				}
				
				when {
					isLoading -> {
						item {
							Column(horizontalAlignment = Alignment.CenterHorizontally) {
								CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
								Spacer(modifier = Modifier.height(12.dp))
								Text("Cargando usuarios...", color = MaterialTheme.colorScheme.onSurfaceVariant)
							}
						}
					}
					
					filteredList.isEmpty() -> {
						item {
							Text(
								text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay personas registradas",
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					}
					
					else -> {
						items(filteredList) { user ->
							PersonCard(user, navEdit, navPag, onViewDetails = {
								selectedUser = user
								showUserDialog = true
							})
						}
					}
				}
			}
		} else {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
			) {
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
				
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 16.dp),
					contentAlignment = Alignment.Center
				) {
					when {
						isLoading -> {
							Column(horizontalAlignment = Alignment.CenterHorizontally) {
								CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
								Spacer(modifier = Modifier.height(12.dp))
								Text("Cargando usuarios...", color = MaterialTheme.colorScheme.onSurfaceVariant)
							}
						}
						
						filteredList.isEmpty() -> {
							Text(
								text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay personas registradas",
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
						
						else -> {
							LazyColumn(
								modifier = Modifier
									.fillMaxSize(),
								verticalArrangement = Arrangement.spacedBy(12.dp)
							) {
								items(filteredList) { user ->
									PersonCard(user, navEdit, navPag, onViewDetails = {
										selectedUser = user
										showUserDialog = true
									})
								}
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun PersonCard(
	user: ListUser,
	navEdit: (String) -> Unit,
	navPag: (String) -> Unit,
	onViewDetails: () -> Unit,
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(2.dp),
		colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = user.name,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold
				)
				Text(
					text = (user.state ?: "Desconocido").capitalize(),
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
						message = """Hola ${user.name}, te recordamos que tu membresía está próxima a vencer.¡Contáctanos para renovarla a tiempo!
             """.trimIndent()
					)
				}
				
				IconButton(onClick = { navPag(user.id) }) {
					Icon(
						Icons.Default.Payment,
						contentDescription = "Pagar",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				IconButton(onClick = { navEdit(user.id) }) {
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
