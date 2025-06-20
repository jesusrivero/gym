package com.jesus.gymcontrol.presentation.ui.settings.details.manage


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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.ListUser
import com.jesus.gymcontrol.domain.viewmodels.UserListViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonsScreen(
	navBottom: NavController,
	viewModel: UserListViewModel = hiltViewModel(),
	navEdit: (String) -> Unit,
	navPag: (String) -> Unit
) {
	LaunchedEffect(Unit) {
		viewModel.loadUsers()
	}
	
	var showUserDialog by remember { mutableStateOf(false) }
	var selectedUser by remember { mutableStateOf<ListUser?>(null) }
	var searchText by remember { mutableStateOf("") }
	var selectedState by remember { mutableStateOf("Todos") }
	
	val filterOptions = listOf("Todos", "Activos", "Inactivos", "Próximos a pagar")
	
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
					DetailRow("Nombre:", selectedUser?.name ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Email:", selectedUser?.email ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Cédula:", selectedUser?.idCard ?: "")
					Spacer(modifier = Modifier.height(8.dp))
					DetailRow("Teléfono:", selectedUser?.phone ?: "")
				}
			},
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
					)
				) {
					Text("Cerrar")
				}
			}
		)
	}
	
	Scaffold(
		topBar = {
			Column {
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
				PaymentFilters(
					selectedPaymentType = selectedState,
					paymentTypeOptions = filterOptions,
					onPaymentTypeSelected = { selectedState = it },
					onClearFilters = {
						selectedState = "Todos"
						searchText = ""
					},
					searchText = searchText,
					onSearchTextChanged = { searchText = it }
				)
			}
		}
	) { innerPadding ->
		val users = viewModel.listUsers
		val isLoading = viewModel.isLoading
		
		val filteredList = users.filter {
			(searchText.isBlank() || it.name.contains(searchText, true)
					|| it.email.contains(searchText, true)
					|| it.idCard.contains(searchText, true)
					|| it.phone.contains(searchText, true))
		}
		
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding),
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
								Row(
									modifier = Modifier
										.fillMaxWidth()
										.padding(horizontal = 16.dp, vertical = 12.dp),
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = user.name,
										style = MaterialTheme.typography.titleMedium,
										fontWeight = FontWeight.SemiBold,
										modifier = Modifier.weight(1f)
									)
									
									Row(
										horizontalArrangement = Arrangement.spacedBy(4.dp),
										verticalAlignment = Alignment.CenterVertically
									) {
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
										IconButton(onClick = {
											selectedUser = user
											showUserDialog = true
										}) {
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
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			Text(
				text = value,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}
