package com.techcode.gymcontrol.presentation.ui.screens.SubScreen


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsScreen(
	navBottom: NavController,
	viewModel: PeopleViewModel,
	navEdit: (Int) -> Unit,
) {
	LaunchedEffect(true) {
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
				Text("Información del usuario",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center)
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
					colors = ButtonDefaults.buttonColors(containerColor = Color(0xBAA7D3DC))
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
				TextButton(
					onClick = {
						datePickerState.selectedDateMillis?.let {
							startDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
						}
						showStartDatePicker = false
					}
				) {
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
				TextButton(
					onClick = {
						datePickerState.selectedDateMillis?.let {
							endDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
						}
						showEndDatePicker = false
					}
				) {
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
							text = "Listado de Personas",
							color = Color.White,
							fontWeight = FontWeight.Bold
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
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = Color(0xBAA7D3DC)
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
					onSearchTextChanged = { searchText = it },
					labelPaymentType = "Filtrar por estado",
					labelSearch = "Buscar personas..."
				)
			}
		}
	) { innerPadding ->
		val state = viewModel.state

		val filteredList = if (searchText.isBlank()) {
			state.userList
		} else {
			state.userList.filter { user ->
				user.usuario.contains(searchText, ignoreCase = true) ||
						user.email.contains(searchText, ignoreCase = true) ||
						user.cedula.contains(searchText, ignoreCase = true)
				        user.numeroTelefono.contains(searchText, ignoreCase = true)
			}
		}

		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
			contentAlignment = Alignment.Center
		) {
			if (filteredList.isEmpty()) {
				Text(
					text = if (searchText.isNotEmpty()) "No se encontraron resultados"
					else "No hay personas registradas",
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
							elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
							colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
						) {
							Column(modifier = Modifier.padding(16.dp)) {
								Row(
									modifier = Modifier.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Column(modifier = Modifier.weight(1f)) {
										Text(
											text = user.usuario,
											style = MaterialTheme.typography.titleMedium,
											fontWeight = FontWeight.SemiBold,
											color = MaterialTheme.colorScheme.onSurface
										)
										Spacer(modifier = Modifier.height(4.dp))


										Row(
											modifier = Modifier.fillMaxWidth(),
											horizontalArrangement = Arrangement.spacedBy(8.dp)
										) {
											PaymentInfoBadge("Email", user.email)
											PaymentInfoBadge("Cédula", user.cedula)
											PaymentInfoBadge("Numero", user.numeroTelefono)
										}

										Spacer(modifier = Modifier.height(8.dp))

										Text(
											text = "Teléfono: ${user.numeroTelefono}",
											style = MaterialTheme.typography.bodyMedium,
											color = MaterialTheme.colorScheme.onSurfaceVariant
										)
										Text(
											text = user.numeroTelefono,
											style = MaterialTheme.typography.titleMedium,
											fontWeight = FontWeight.SemiBold,
											color = MaterialTheme.colorScheme.onSurface
										)
									}

									IconButton(
										onClick = { selectedUser = user; showUserDialog = true },
										modifier = Modifier.size(40.dp)
									) {
										Icon(
											painter = painterResource(id = R.drawable.ic_details),
											contentDescription = "Detalles",
											tint = MaterialTheme.colorScheme.primary
										)
									}
								}

								Divider(
									modifier = Modifier
										.fillMaxWidth()
										.padding(vertical = 8.dp),
									color = MaterialTheme.colorScheme.outlineVariant,
									thickness = 0.5.dp
								)

								Row(
									modifier = Modifier.fillMaxWidth(),
									horizontalArrangement = Arrangement.End
								) {
									IconButton(
										onClick = { user.id?.let { navEdit(it) } },
										modifier = Modifier.size(40.dp)
									) {
										Icon(
											imageVector = Icons.Default.Edit,
											contentDescription = "Editar",
											tint = MaterialTheme.colorScheme.primary
										)
									}

									IconButton(
										onClick = { viewModel.deleteUser(user) },
										modifier = Modifier.size(40.dp)
									) {
										Icon(
											imageVector = Icons.Default.Delete,
											contentDescription = "Eliminar",
											tint = MaterialTheme.colorScheme.error
										)
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


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentFilters(
	selectedPaymentType: String,
	paymentTypeOptions: List<String>,
	onPaymentTypeSelected: (String) -> Unit,
	startDate: LocalDate?,
	endDate: LocalDate?,
	onStartDateClick: () -> Unit,
	onEndDateClick: () -> Unit,
	onClearFilters: () -> Unit,
	dateFormatter: DateTimeFormatter,
	searchText: String,
	onSearchTextChanged: (String) -> Unit,
	labelPaymentType: String = "Tipo de pago",
	labelSearch: String = "Buscar..."
) {
	var expandedFilter by remember { mutableStateOf(false) }

	Column {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp),
			shape = RoundedCornerShape(16.dp),
			color = MaterialTheme.colorScheme.surface,
			shadowElevation = 4.dp
		) {
			Column(modifier = Modifier.padding(16.dp)) {


				Text(
					text = labelPaymentType,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
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
						trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilter) },
						modifier = Modifier
							.fillMaxWidth()
							.menuAnchor(),
						shape = RoundedCornerShape(12.dp)
					)

					ExposedDropdownMenu(
						expanded = expandedFilter,
						onDismissRequest = { expandedFilter = false }
					) {
						paymentTypeOptions.forEach { option ->
							DropdownMenuItem(
								text = { Text(option, modifier = Modifier.fillMaxWidth()) },
								onClick = { onPaymentTypeSelected(option); expandedFilter = false }
							)
						}
					}
				}

				Spacer(modifier = Modifier.height(16.dp))


				Text(
					text = "Rango de fechas",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(bottom = 4.dp)
				)

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					OutlinedButton(
						onClick = onStartDateClick,
						modifier = Modifier.weight(1f),
						shape = RoundedCornerShape(12.dp),
						colors = ButtonDefaults.outlinedButtonColors(
							containerColor = MaterialTheme.colorScheme.surface,
							contentColor = MaterialTheme.colorScheme.onSurface
						)
					) {
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							Text(
								text = "Desde",
								style = MaterialTheme.typography.labelSmall,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
							Text(
								text = startDate?.format(dateFormatter) ?: "Seleccionar",
								style = MaterialTheme.typography.bodyMedium,
								color = if (startDate != null) MaterialTheme.colorScheme.onSurface
								else MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					}

					OutlinedButton(
						onClick = onEndDateClick,
						modifier = Modifier.weight(1f),
						shape = RoundedCornerShape(12.dp),
						colors = ButtonDefaults.outlinedButtonColors(
							containerColor = MaterialTheme.colorScheme.surface,
							contentColor = MaterialTheme.colorScheme.onSurface
						)
					) {
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							Text(
								text = "Hasta",
								style = MaterialTheme.typography.labelSmall,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
							Text(
								text = endDate?.format(dateFormatter) ?: "Seleccionar",
								style = MaterialTheme.typography.bodyMedium,
								color = if (endDate != null) MaterialTheme.colorScheme.onSurface
								else MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					}
				}


				if (selectedPaymentType != "Todos" || startDate != null || endDate != null) {
					Spacer(modifier = Modifier.height(16.dp))
					OutlinedButton(
						onClick = onClearFilters,
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.outlinedButtonColors(
							contentColor = MaterialTheme.colorScheme.primary
						),
						border = BorderStroke(
							1.dp,
							MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
						)
					) {
						Icon(
							imageVector = Icons.Default.Close,
							contentDescription = "Limpiar",
							modifier = Modifier.size(18.dp)
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text("Limpiar filtros")
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
			placeholder = { Text(labelSearch) },
			leadingIcon = {
				Icon(
					Icons.Default.Search,
					contentDescription = "Buscar",
					tint = MaterialTheme.colorScheme.primary
				)
			},
			shape = RoundedCornerShape(16.dp),
			colors = TextFieldDefaults.colors(
				focusedContainerColor = MaterialTheme.colorScheme.surface,
				unfocusedContainerColor = MaterialTheme.colorScheme.surface,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				focusedTextColor = MaterialTheme.colorScheme.onSurface,
				unfocusedTextColor = MaterialTheme.colorScheme.onSurface
			),
			singleLine = true,
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
		)
	}
}


@Composable
private fun DetailRow(label: String, value: String) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}

@Composable
private fun PaymentInfoBadge(label: String, value: String) {
	Surface(
		shape = RoundedCornerShape(8.dp),
		color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
		contentColor = MaterialTheme.colorScheme.primary
	) {
		Column(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = label,
				style = MaterialTheme.typography.labelSmall
			)
			Text(
				text = value,
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Bold
			)
		}
	}
}