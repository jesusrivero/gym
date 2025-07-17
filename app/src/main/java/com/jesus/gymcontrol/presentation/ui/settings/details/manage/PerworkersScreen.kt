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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.model.getAdmin.AdminSummary
import com.jesus.gymcontrol.domain.viewmodels.RegisterUserFromAdminViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerworkersScreen(
	viewModel: RegisterUserFromAdminViewModel = hiltViewModel(),
	navController: NavController,
) {
	val context = LocalContext.current
	
	val isLoading by viewModel.isLoading.collectAsState()
	val admins by viewModel.admins.collectAsState()
	val errorMessage = viewModel.errorMessage
	
	// Estado local para saber qué admin se quiere desactivar o habilitar
	var adminToConfirm by remember { mutableStateOf<AdminSummary?>(null) }
	
	// Estado para mostrar diálogo de éxito
	var showSuccessDialog by remember { mutableStateOf(false) }
	
	// Observa el registerSuccess del ViewModel para activar diálogo
	
	
	LaunchedEffect(viewModel.registerSuccess) {
		if (viewModel.registerSuccess) {
			showSuccessDialog = true
			delay(1000) // Espera 1 segundos antes de cerrar el diálogo
			showSuccessDialog = false
			viewModel.resetRegisterState()
		}
	}
	
	LaunchedEffect(Unit) {
		viewModel.loadAdmins()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Administradores",
						style = MaterialTheme.typography.titleLarge,
						color = MaterialTheme.colorScheme.onPrimary
					)
				},
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
						Icon(
							imageVector = Icons.Default.ArrowBack,
							contentDescription = "Regresar",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primary
				)
			)
		}
	) { padding ->
		Box(
			modifier = Modifier
				.padding(padding)
				.fillMaxSize()
		) {
			when {
				isLoading -> {
					CircularProgressIndicator(
						modifier = Modifier.align(Alignment.Center),
						color = MaterialTheme.colorScheme.primary
					)
				}
				
				admins.isEmpty() -> {
					Text(
						text = "No hay administradores registrados.",
						modifier = Modifier.align(Alignment.Center),
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				
				else -> {
					LazyColumn(
						modifier = Modifier
							.padding(horizontal = 16.dp, vertical = 8.dp)
							.fillMaxSize(),
						verticalArrangement = Arrangement.spacedBy(12.dp)
					) {
						items(admins) { admin ->
							Card(
								modifier = Modifier.fillMaxWidth(),
								elevation = CardDefaults.cardElevation(4.dp),
								colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
							) {
								Row(
									modifier = Modifier
										.fillMaxWidth()
										.padding(16.dp),
									horizontalArrangement = Arrangement.SpaceBetween,
									verticalAlignment = Alignment.CenterVertically
								) {
									Column {
										Text(
											text = "${admin.name} ${admin.lastname}",
											style = MaterialTheme.typography.titleMedium,
											color = MaterialTheme.colorScheme.primary
										)
										Spacer(Modifier.height(4.dp))
										Text(
											text = "Correo: ${admin.email}",
											style = MaterialTheme.typography.bodyMedium
										)
										Text(
											text = "Estado: ${admin.state}",
											style = MaterialTheme.typography.bodyMedium
										)
									}
									Button(
										onClick = {
											adminToConfirm = admin
										},
										colors = ButtonDefaults.buttonColors(
											containerColor =
												if (admin.state == "activo") MaterialTheme.colorScheme.error
												else MaterialTheme.colorScheme.primary
										)
									) {
										Text(
											if (admin.state == "activo") "Deshabilitar" else "Habilitar",
											color = MaterialTheme.colorScheme.onPrimary
										)
									}
								}
							}
						}
					}
				}
			}
			
			errorMessage?.let {
				LaunchedEffect(it) {
					Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
					viewModel.clearError()
				}
			}
		}
	}
	
	adminToConfirm?.let { admin ->
		val isActivating = admin.state != "activo"
		AlertDialog(
			onDismissRequest = { adminToConfirm = null },
			title = {
				Text(
					text = if (isActivating) "activar administrador" else "Desactivar administrador",
					modifier = Modifier
						.fillMaxWidth()
						.wrapContentWidth(Alignment.CenterHorizontally)
				)
			},
			text = {
				Text(
					text = "¿Estás seguro de que deseas " +
							(if (isActivating) "activar" else "desactivar") +
							" a ${admin.name} ${admin.lastname}?",
					modifier = Modifier
						.fillMaxWidth()
						.wrapContentWidth(Alignment.CenterHorizontally)
				)
			},
			confirmButton = {
				TextButton(onClick = {
					adminToConfirm = null
					val newState = if (isActivating) "activo" else "inactivo"
					viewModel.updateAdminState(admin, newState)
				}) {
					Text("Aceptar")
				}
			},
			dismissButton = {
				TextButton(onClick = {
					adminToConfirm = null
				}) {
					Text("Cancelar")
				}
			},
			containerColor = MaterialTheme.colorScheme.surface
		)
	}

	
	
	// AlertDialog para éxito
	if (showSuccessDialog) {
		val colorScheme = MaterialTheme.colorScheme
		AlertDialog(
			onDismissRequest = { showSuccessDialog = false },
			title = { Text("¡Éxito!") },
			text = { Text("Información actualizada con éxito.") },
			confirmButton = {},
			icon = {
				Icon(
					imageVector = Icons.Default.CheckCircle,
					contentDescription = null,
					tint = Color(0xFF4CAF50)
				)
			},
			containerColor = MaterialTheme.colorScheme.surface,
			titleContentColor = MaterialTheme.colorScheme.onSurface,
			textContentColor = MaterialTheme.colorScheme.onSurface
		)
	}
}

