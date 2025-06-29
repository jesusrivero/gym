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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesus.gymcontrol.presentation.theme.GymTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
	navController: NavController,
	notifications: List<String> = listOf(
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'",
		"Membresía vencida: Ana G.",
		"Nuevo registro: Juan M.",
		"Pago recibido: 15 USD",
		"Cambio de estado: Laura ahora está 'Inactiva'"
	)
) {
	GymTheme {
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
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.verticalScroll(rememberScrollState())
					.padding(16.dp)
			) {
				if (notifications.isEmpty()) {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						Text(
							text = "No hay notificaciones por mostrar.",
							style = MaterialTheme.typography.bodyMedium,
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)
					}
				} else {
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
							Text(
								text = notification,
								style = MaterialTheme.typography.bodyMedium,
								modifier = Modifier.padding(16.dp)
							)
						}
					}
				}
			}
		}
	}
}