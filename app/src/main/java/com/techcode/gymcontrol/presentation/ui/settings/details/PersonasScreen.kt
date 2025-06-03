package com.techcode.gymcontrol.presentation.ui.settings.details


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.presentation.theme.GymTheme
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonsScreen(
	navBottom: NavController,
	viewModel: PeopleViewModel,
	navEdit: (Int) -> Unit,
//	navPag: (Int) -> Unit
) {
	LaunchedEffect(Unit) {
		viewModel.getUsers()
	}
	
	var showUserDialog by remember { mutableStateOf(false) }
	var selectedUser by remember { mutableStateOf<Person?>(null) }
	var searchText by remember { mutableStateOf("") }
	var selectedState by remember { mutableStateOf("Todos") }
	var startDate by remember { mutableStateOf<LocalDate?>(null) }
	var endDate by remember { mutableStateOf<LocalDate?>(null) }
	var showStartDatePicker by remember { mutableStateOf(false) }
	var showEndDatePicker by remember { mutableStateOf(false) }
	
	val filterOptions = listOf("Todos", "Activos", "Inactivos", "Próximos a pagar")
	val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
	
	if (showUserDialog && selectedUser != null) {
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
					DetailRow("Nombre:", selectedUser?.usuario ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Email:", selectedUser?.email ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Cédula:", selectedUser?.cedula ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Teléfono:", selectedUser?.numeroTelefono ?: "")
				}
			},
			confirmButton = {
				Button(
					onClick = { showUserDialog = false },
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = colorScheme.primary,
						contentColor = colorScheme.onPrimary
					)
				) {
					Text("Cerrar")
				}
			}
		)
	}
	
	if (showStartDatePicker) {
		val datePickerState = rememberDatePickerState()
		DatePickerDialog(
			onDismissRequest = { showStartDatePicker = false },
			confirmButton = {
				TextButton(onClick = {
					datePickerState.selectedDateMillis?.let {
						startDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
					}
					showStartDatePicker = false
				}) {
					Text("OK")
				}
			}
		) {
			DatePicker(state = datePickerState, title = { Text("Seleccionar fecha inicial") })
		}
	}
	
	if (showEndDatePicker) {
		val datePickerState = rememberDatePickerState()
		DatePickerDialog(
			onDismissRequest = { showEndDatePicker = false },
			confirmButton = {
				TextButton(onClick = {
					datePickerState.selectedDateMillis?.let {
						endDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
					}
					showEndDatePicker = false
				}) {
					Text("OK")
				}
			}
		) {
			DatePicker(state = datePickerState, title = { Text("Seleccionar fecha final") })
		}
	}
	
	Scaffold(
		topBar = {
			Column {
				TopAppBar(
					title = {
						Text(
							text = "Listado de personas",
							color = colorScheme.onPrimary,
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
						containerColor = colorScheme.primary
					)
				)
				PaymentFilters(
					selectedPaymentType = selectedState,
					paymentTypeOptions = filterOptions,
					onPaymentTypeSelected = { selectedState = it },
					startDate = startDate,
					endDate = endDate,
					onStartDateClick = { showStartDatePicker = true },
					onEndDateClick = { showEndDatePicker = true },
					onClearFilters = {
						selectedState = "Todos"
						startDate = null
						endDate = null
					},
					dateFormatter = dateFormatter,
					searchText = searchText,
					onSearchTextChanged = { searchText = it }
				)
			}
		}
	) { innerPadding ->
		val state = viewModel.state
		val filteredList = state.userList.filter {
			(searchText.isBlank() || it.usuario.contains(searchText, true)
					|| it.email.contains(searchText, true)
					|| it.cedula.contains(searchText, true)
					|| it.numeroTelefono.contains(searchText, true))
		}
		
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			if (filteredList.isEmpty()) {
				Text(
					text = if (searchText.isNotEmpty()) "No se encontraron resultados" else "No hay personas registradas",
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			} else {
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 16.dp),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					items(filteredList) { user ->
						Card(
							modifier = Modifier.fillMaxWidth(),
							shape = RoundedCornerShape(16.dp),
							elevation = CardDefaults.cardElevation(2.dp),
							colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
						) {
							Column(modifier = Modifier.padding(16.dp)) {
								Row(verticalAlignment = Alignment.CenterVertically) {
									Column(modifier = Modifier.weight(1f)) {
										Text(
											user.usuario,
											style = MaterialTheme.typography.titleMedium,
											fontWeight = FontWeight.SemiBold
										)
										Spacer(modifier = Modifier.height(4.dp))
										Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
											PaymentInfoBadge("Email", user.email)
											PaymentInfoBadge("Cédula", user.cedula)
											PaymentInfoBadge("Número", user.numeroTelefono)
										}
									}
									IconButton(
										onClick = { selectedUser = user; showUserDialog = true },
										modifier = Modifier.size(40.dp)
									) {
										Icon(
											painter = painterResource(id = R.drawable.ic_details),
											contentDescription = "Detalles",
											tint = colorScheme.primary
										)
									}
								}
								Divider(
									modifier = Modifier.padding(vertical = 8.dp),
									color = colorScheme.outlineVariant,
									thickness = 0.5.dp
								)
								Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
//									IconButton(onClick = { user.id?.let { navPag(it) } }) {
//										Icon(Icons.Default.Payment, "Pagar", tint = colorScheme.primary)
//									}
									IconButton(onClick = { user.id?.let { navEdit(it) } }) {
										Icon(Icons.Default.Edit, "Editar", tint = colorScheme.primary)
									}
									IconButton(onClick = { viewModel.deleteUser(user) }) {
										Icon(Icons.Default.Delete, "Eliminar", tint = colorScheme.error)
									}
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
private fun DetailRow(label: String, value: String) {
	GymTheme {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				text = label,
				fontWeight = FontWeight.Bold,
				color = colorScheme.onSurfaceVariant
			)
			Text(
				text = value,
				color = colorScheme.onSurface
			)
		}
		
	}
	
}

@Composable
private fun PaymentInfoBadge(label: String, value: String) {
	Surface(
		shape = RoundedCornerShape(8.dp),
		color = colorScheme.primary.copy(alpha = 0.1f),
		contentColor = colorScheme.primary
	) {
		Column(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(text = label, style = MaterialTheme.typography.labelSmall)
			Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
		}
	}
}