package com.jesus.gymcontrol.presentation.ui.settings.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes


@Composable
fun OwnerMainScreen(
	navController: NavController,
	viewModel: AuthViewModel = hiltViewModel(),
	gymViewModel: GymViewModel
) {
	val context = LocalContext.current
	val sessionManager = remember { SessionManager(context) }

	val selectedRole = gymViewModel.setSelectedRoleForCode
	val generatedCode = gymViewModel.generatedCode
	val isGenerating = gymViewModel.isGenerating
	val errorMessage = gymViewModel.errorMessage

	val coroutineScope = rememberCoroutineScope()

	// Cargar código de gimnasio del dueño (si no se ha cargado)
	LaunchedEffect(Unit) {
		val uid = FirebaseAuth.getInstance().currentUser?.uid
		if (uid != null && gymViewModel.gymCode == null) {
			gymViewModel.loadCurrentUserGymCode(uid)
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(modifier = Modifier.height(40.dp))

		Icon(
			imageVector = Icons.Default.FitnessCenter,
			contentDescription = "Icono gimnasio",
			tint = MaterialTheme.colorScheme.primary,
			modifier = Modifier.size(72.dp)
		)

		Text(
			text = "Bienvenido al gimnasio",
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Bold
		)

		Text(
			text = "Aquí podrás administrar tu gimnasio",
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Bold
		)

		// Tarjeta resumen
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(120.dp),
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.primaryContainer
			)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Tu progreso",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Medium
				)
				Text(
					text = "Sesiones activas: 3\nÚltima visita: Hace 2 días",
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}

		// Selección de rol
		Text("Selecciona el rol para generar el código:")
		Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
			listOf("cliente", "administrador").forEach { role ->
				Row(verticalAlignment = Alignment.CenterVertically) {
					RadioButton(
						selected = gymViewModel.setSelectedRoleForCode == role,
						onClick = { gymViewModel.SetSelectedRoleForCode(role) }
					)
					Text(role.replaceFirstChar { it.uppercase() })
				}
			}
		}

		// Botón para generar código
		Button(
			onClick = {
				gymViewModel.generateCodeForRole()
			},
			enabled = !isGenerating
		) {
			if (isGenerating) {
				CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
				Spacer(modifier = Modifier.width(8.dp))
			}
			Text("Generar código")
		}

		// Mostrar código generado
		generatedCode?.let { code ->
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text("Código generado:", fontWeight = FontWeight.Bold)
				Text(code, style = MaterialTheme.typography.titleMedium)

				Spacer(modifier = Modifier.height(8.dp))
				OutlinedButton(onClick = {
					val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
					clipboard.setPrimaryClip(ClipData.newPlainText("Código", code))
					Toast.makeText(context, "Código copiado al portapapeles", Toast.LENGTH_SHORT).show()
				}) {
					Icon(Icons.Default.ContentCopy, contentDescription = "Copiar")
					Spacer(modifier = Modifier.width(8.dp))
					Text("Copiar")
				}
			}
		}

		// Mostrar errores (opcional)
		errorMessage?.let {
			Text(
				text = it,
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.bodyMedium
			)
		}

		// Cerrar sesión
		OutlinedButton(
			onClick = {
				sessionManager.clearSession()
				FirebaseAuth.getInstance().signOut()
				navController.navigate(AppRoutes.LoginScreen) {
					popUpTo(0) { inclusive = true }
				}
			},
			modifier = Modifier.fillMaxWidth(),
			colors = ButtonDefaults.outlinedButtonColors(
				contentColor = MaterialTheme.colorScheme.error
			)
		) {
			Icon(
				imageVector = Icons.Default.ExitToApp,
				contentDescription = "Cerrar sesión",
				tint = MaterialTheme.colorScheme.error
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text("Cerrar sesión")
		}
	}
}