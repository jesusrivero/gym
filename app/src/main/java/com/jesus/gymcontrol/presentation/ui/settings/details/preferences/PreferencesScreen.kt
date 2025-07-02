package com.jesus.gymcontrol.presentation.ui.settings.details.preferences


import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.jesus.gymcontrol.presentation.ui.commons.NotificationPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(navController: NavController) {
	GymTheme {
		PreferencesContent(navController = navController)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesContent(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	val context = LocalContext.current
	var showNotifications by remember { mutableStateOf(false) }
	val notifications = listOf(
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'",
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'",
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'",
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'",
		"Nuevo registro: Jesús R.",
		"Pago recibido: 20 USD",
		"Cambio de estado: Pedro ahora está 'Pendiente'"
	)
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Preferencias",
						color = colorScheme.onPrimary,
						fontWeight = FontWeight.Bold
					)
				}, actions = {
					IconButton(onClick = { showNotifications = !showNotifications }) {
						Icon(
							imageVector = Icons.Default.Notifications,
							contentDescription = "Notificaciones",
							tint = colorScheme.onPrimary
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = colorScheme.primary
				)
			)
		},
		bottomBar = {
			BottomNavigationBar(navController = navController)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(horizontal = 4.dp)
				.verticalScroll(rememberScrollState())
		) {
			SettingsSectionTitle("General")
			
			SettingsItem(
				text = "Cuenta y Datos",
				icon = Icons.Default.AccountCircle,
				onClick = { navController.navigate(AppRoutes.AccountScreen) }
			)
			
			SettingsItem(
				text = "Notificaciones",
				icon = Icons.Default.Notifications,
				onClick = { navController.navigate(AppRoutes.NotificationScreen) }
			)
			
			SettingsItem(
				text = "Seguridad",
				icon = Icons.Default.Build,
				onClick = { navController.navigate(AppRoutes.SecurityScreen) }
			)
			SettingsItem(
				text = "Generar codigo para clientes",
				icon = Icons.Default.Info,
				onClick = { navController.navigate(AppRoutes.CodeClientScreen) }
			)
			
			Spacer(modifier = Modifier.height(24.dp))
			
			SettingsSectionTitle("Soporte")
			
			SettingsItem(
				text = "Reportar errores",
				icon = Icons.Default.Email,
				onClick = {
					val intent = Intent(Intent.ACTION_SEND).apply {
						type = "message/rfc822"
						putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@tugimnasio.com"))
						putExtra(Intent.EXTRA_SUBJECT, "Reporte de error")
						putExtra(Intent.EXTRA_TEXT, "Hola, encontré un error en la app:")
						setPackage("com.google.android.gm")
					}
					
					try {
						context.startActivity(Intent.createChooser(intent, "Enviar correo con..."))
					} catch (e: ActivityNotFoundException) {
						Toast.makeText(context, "No se encontró una app de correo", Toast.LENGTH_SHORT).show()
					}
				}
			)
			
			
			SettingsItem(
				text = "Sobre nosotros",
				icon = Icons.Default.Info,
				onClick = { navController.navigate(AppRoutes.ContactScreen) }
			)
			
			Spacer(modifier = Modifier.padding(vertical = 20.dp))
			
			Button(
				onClick = {
					viewModel.logout()
					navController.navigate(AppRoutes.LoginScreen) {
						popUpTo(AppRoutes.PreferencesScreen) { inclusive = true }
					}
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
			) {
				Text(text = "Cerrar sesión")
			}
		}
	}
	NotificationPanel(
		isVisible = showNotifications,
		navController = navController,
		notifications = notifications,
		onDismiss = { showNotifications = false }
	)
}

@Composable
fun SettingsSectionTitle(title: String) {
	Text(
		text = title,
		style = MaterialTheme.typography.headlineSmall.copy(
			fontWeight = FontWeight.Bold
		),
		color = MaterialTheme.colorScheme.onSurface,
		modifier = Modifier
			.padding(horizontal = 16.dp, vertical = 12.dp)
	)
}

@Composable
fun SettingsItem(
	text: String,
	icon: ImageVector,
	onClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	
	Column {
		TextButton(
			onClick = onClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp),
			colors = ButtonDefaults.textButtonColors(
				contentColor = colorScheme.onSurface
			)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 8.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Start
			) {
				Icon(
					imageVector = icon,
					contentDescription = null,
					tint = colorScheme.onSurface.copy(alpha = 0.7f),
					modifier = Modifier.size(24.dp)
				)
				
				Spacer(modifier = Modifier.width(12.dp))
				
				Text(
					text = text,
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier.weight(1f)
				)
				
				Icon(
					imageVector = Icons.Default.KeyboardArrowRight,
					contentDescription = "Navegar",
					tint = colorScheme.onSurface.copy(alpha = 0.5f)
				)
			}
		}
		
		Divider(
			color = colorScheme.onSurface.copy(alpha = 0.1f),
			thickness = 1.dp,
			modifier = Modifier.padding(start = 56.dp, end = 16.dp)
		)
	}
}

@Preview(showBackground = true)
@Composable
fun PreferencesPreview() {
	GymTheme {
		PreferencesContent(
			navController = NavController(LocalContext.current)
		)
	}
}