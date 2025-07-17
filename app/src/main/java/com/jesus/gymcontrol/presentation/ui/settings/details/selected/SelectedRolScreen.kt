package com.jesus.gymcontrol.presentation.ui.settings.details.selected

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedRolScreen(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel(),
	sessionManager: SessionManager
) {
	SelectedRolScreenContent(
		navController = navController,
		viewModel = viewModel,
		sessionManager = sessionManager
	)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedRolScreenContent(
	navController: NavController,
	viewModel: AuthViewModel,
	sessionManager: SessionManager
) {
	Scaffold(
//		topBar = {
//			CenterAlignedTopAppBar(
//				title = {
//					Text(
//						text = "Elige tu rol",
//						color = Color.White,
//						fontWeight = FontWeight.SemiBold
//					)
//				},
//				colors = TopAppBarDefaults.topAppBarColors(
//					containerColor = MaterialTheme.colorScheme.primary
//				)
//			)
//		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(15.dp)
				
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {
			
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 16.dp),
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer
					),
					elevation = CardDefaults.cardElevation(4.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 20.dp, vertical = 16.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "¿Cómo deseas ingresar?",
							style = MaterialTheme.typography.titleMedium.copy(
								fontWeight = FontWeight.Bold,
								color = MaterialTheme.colorScheme.onPrimaryContainer
							),
							textAlign = TextAlign.Center
						)
						Spacer(modifier = Modifier.height(8.dp))
						Text(
							text = "Selecciona una de las siguientes opciones para continuar",
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
							),
							textAlign = TextAlign.Center
						)
					}
				}
				
				MenuCardRol(
					title = "Dueño de Gimnasio",
					description = "Como dueño, tendrás control total sobre la configuración de tu gimnasio: podrás digitalizar tu negocio, gestionar al personal (administradores) y mantener un seguimiento completo de los clientes y sus pagos. Es la opción ideal si eres el propietario y responsable principal.",
					icon = Icons.Default.Home,
					onClick = {
						val uid = sessionManager.getUserUid() ?: return@MenuCardRol
						sessionManager.setUserSessionData(uid, "Dueño", "")
						viewModel.rol = "Dueño"
						navController.navigate(AppRoutes.ActivateCodeScreen)
					}
				)
				
				MenuCardRol(
					title = "Administrador",
					description = "Como administrador, puedes encargarte de gestionar las operaciones diarias del gimnasio, apoyar al dueño en las tareas administrativas y garantizar que los servicios funcionen correctamente. Es la mejor opción si te asignaron para ayudar a manejar el negocio.",
					icon = Icons.Default.ManageAccounts,
					onClick = {
						val uid = sessionManager.getUserUid() ?: return@MenuCardRol
						sessionManager.setUserSessionData(uid, "Administrador", "")
						viewModel.rol2 = "Administrador"
						navController.navigate(AppRoutes.SelectedGymAdmin)
					}
				)
				
				Spacer(modifier = Modifier.height(16.dp))
				
				//            MenuCardRol(
//                title = "Cliente",
//                description = "Accede como cliente de un gimnasio",
//                icon = Icons.Default.Person,
//                onClick = {
//                    viewModel.rol3 = "Cliente"
//                    navController.navigate(AppRoutes.SelectedGymClient)
//                }
//            )
				
				Button(
					onClick = {
						viewModel.logout()
						navController.navigate(AppRoutes.LoginScreen) {
							popUpTo(AppRoutes.PreferencesScreen) { inclusive = true }
						}
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 16.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primary
					)
				) {
					Text(text = "Cerrar sesión")
				}
			}
		}
		
		Spacer(modifier = Modifier.height(24.dp))
	}
}


@Composable
fun MenuCardRol(
	title: String,
	description: String,
	icon: ImageVector,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		onClick = onClick,
		shape = RoundedCornerShape(12.dp),
		elevation = CardDefaults.cardElevation(2.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Box(
					modifier = Modifier
						.size(48.dp)
						.background(
							color = MaterialTheme.colorScheme.primaryContainer,
							shape = RoundedCornerShape(8.dp)
						),
					contentAlignment = Alignment.Center
				) {
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.onPrimaryContainer
					)
				}
				Spacer(modifier = Modifier.width(12.dp))
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
					color = MaterialTheme.colorScheme.onSurface
				)
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Text(
				text = description,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}


