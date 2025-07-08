package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.viewmodels.notification.NotificacionesViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.jesus.gymcontrol.domain.model.notification.Notificacion


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
	navController: NavController,
	viewModel: NotificacionesViewModel = hiltViewModel()
) {
	GymTheme {
		val notifications by viewModel.notifications.collectAsState()
		val isLoading by viewModel.isLoading.collectAsState()
		val error by viewModel.error.collectAsState()
		
		LaunchedEffect(Unit) {
			viewModel.loadNotifications()
			viewModel.marcarTodasComoLeidas()
		}
		
		Scaffold(
			topBar = {
				TopAppBar(
					title = {
						Row(
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								"Notificaciones",
								fontWeight = FontWeight.Bold,
								color = MaterialTheme.colorScheme.onPrimary
							)
							
						}
					},
					navigationIcon = {
						IconButton(onClick = { navController.popBackStack() }) {
							Icon(
								Icons.Default.ArrowBack,
								contentDescription = "Volver",
								tint = MaterialTheme.colorScheme.onPrimary
							)
						}
					},
					actions = {
						if (notifications.isNotEmpty()) {
							IconButton(onClick = { viewModel.deleteAllNotifications() }) {
								Icon(
									imageVector = Icons.Default.DeleteSweep,
									contentDescription = "Eliminar todas",
									tint = MaterialTheme.colorScheme.onPrimary
								)
							}
						}
					},
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = MaterialTheme.colorScheme.primary
					)
				)
			}
		) { innerPadding ->

			Box(
				Modifier
					.fillMaxSize()
					.padding(innerPadding)
			) {
				when {
					isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
					
					error != null -> Text(
						text = "Error: $error",
						color = MaterialTheme.colorScheme.error,
						style = MaterialTheme.typography.bodyMedium,
						modifier = Modifier.align(Alignment.Center)
					)
					
					notifications.isEmpty() -> Text(
						text = "No hay notificaciones por mostrar.",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						modifier = Modifier.align(Alignment.Center)
					)
					
					else -> Column(
						Modifier
							.fillMaxSize()
							.padding(8.dp),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						LazyColumn(
							modifier = Modifier.fillMaxSize(),
							verticalArrangement = Arrangement.spacedBy(8.dp)
						) {
							items(notifications) { notification ->
								NotificationCard(notification)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun NotificationCard(notification: Notificacion) {
	val date = remember(notification.fecha) {
		java.text.SimpleDateFormat(
			"dd/MM/yyyy HH:mm", java.util.Locale.getDefault()
		).format(java.util.Date(notification.fecha))
	}
	
	var showDetails by remember { mutableStateOf(false) }
	
	Card(
		modifier = Modifier
			.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(2.dp),
		shape = RoundedCornerShape(12.dp) // bordes más suaves
	) {
		Column(modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 4.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.Notifications,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary,
					modifier = Modifier.size(24.dp)
				)
				
				Spacer(modifier = Modifier.width(8.dp))
				
				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = notification.titulo,
						style = MaterialTheme.typography.titleSmall,
						fontWeight = FontWeight.SemiBold,
						maxLines = 1
					)
					Text(
						text = date,
						style = MaterialTheme.typography.labelSmall,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				
				IconButton(
					onClick = { showDetails = true },
					modifier = Modifier.size(32.dp)
				) {
					Icon(
						imageVector = Icons.Default.Info,
						contentDescription = "Ver detalles",
						tint = MaterialTheme.colorScheme.primary
					)
				}
			}
			
			Spacer(modifier = Modifier.height(6.dp))
			
			Text(
				text = notification.mensaje,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
	
	if (showDetails) {
		AlertDialog(
			containerColor = MaterialTheme.colorScheme.surface,
			onDismissRequest = { showDetails = false },
			confirmButton = {
				TextButton(onClick = { showDetails = false }) {
					Text("Cerrar")
				}
			},
			title = {
				Text(
					notification.titulo,
					fontWeight = FontWeight.Bold,
					style = MaterialTheme.typography.titleMedium
				)
			},
			text = {
				Text(notification.mensaje, style = MaterialTheme.typography.bodyMedium)
			}
		)
	}
}

