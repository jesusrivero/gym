package com.jesus.gymcontrol.presentation.ui.settings.details.preferences

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
		}
		
		Scaffold(
			topBar = {
				TopAppBar(
					title = {
						Text(
							text = "Notificaciones",
							color = MaterialTheme.colorScheme.onPrimary,
							fontWeight = FontWeight.Bold
						)
					},
					navigationIcon = {
						IconButton(onClick = { navController.popBackStack() }) {
							Icon(
								imageVector = Icons.Default.ArrowBack,
								contentDescription = "Volver",
								tint = MaterialTheme.colorScheme.onPrimary
							)
						}
					},
					colors = TopAppBarDefaults.topAppBarColors(
						containerColor = MaterialTheme.colorScheme.primary
					)
				)
			}
		) { innerPadding ->
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.padding(16.dp)
			) {
				when {
					isLoading -> {
						CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
					}
					error != null -> {
						Text(
							text = "Error: $error",
							color = MaterialTheme.colorScheme.error,
							style = MaterialTheme.typography.bodyMedium,
							modifier = Modifier.align(Alignment.Center)
						)
					}
					notifications.isEmpty() -> {
						Text(
							text = "No hay notificaciones por mostrar.",
							style = MaterialTheme.typography.bodyMedium,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
							modifier = Modifier.align(Alignment.Center)
						)
					}
					else -> {
						Column(
							modifier = Modifier
								.verticalScroll(rememberScrollState())
						) {
							notifications.forEach { notification ->
								Card(
									modifier = Modifier
										.fillMaxWidth()
										.padding(vertical = 6.dp),
									colors = CardDefaults.cardColors(
										containerColor = MaterialTheme.colorScheme.surfaceVariant
									),
									elevation = CardDefaults.cardElevation(2.dp)
								) {
									Column(modifier = Modifier.padding(16.dp)) {
										Text(
											text = notification.mensaje,
											style = MaterialTheme.typography.bodyMedium
										)
										notification.fecha?.let {
											Text(
												text = it.toDate().toString(), // puedes formatear si quieres
												style = MaterialTheme.typography.labelSmall,
												color = MaterialTheme.colorScheme.onSurfaceVariant
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