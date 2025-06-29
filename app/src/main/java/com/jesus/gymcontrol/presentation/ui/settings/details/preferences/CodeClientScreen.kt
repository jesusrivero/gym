package com.jesus.gymcontrol.presentation.ui.settings.details.preferences


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeClientScreen(navController: NavController) {
    GymTheme {
        CodeClientContent(navBottom = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeClientContent(
	navBottom: NavController,
	viewModel: GymViewModel = hiltViewModel()
) {
	val colorScheme = MaterialTheme.colorScheme
	var showDialog by remember { mutableStateOf(false) }
	val generatedCode = viewModel.generatedCode
	val errorMessage = viewModel.errorMessage
	val snackbarHostState = remember { SnackbarHostState() }
	
	val gymCode = viewModel.gymCode
	val userUid = FirebaseAuth.getInstance().currentUser?.uid
	val clipboardManager =  LocalClipboardManager.current
	val context = LocalContext.current
	
	LaunchedEffect(Unit) {
		userUid?.let { viewModel.loadCurrentUserGymCode(it) }
	}
	
	LaunchedEffect(errorMessage) {
		errorMessage?.let {
			snackbarHostState.showSnackbar(it)
			viewModel.clearErrorMessage()
		}
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Administración de códigos",
						color = colorScheme.onPrimary,
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
					containerColor = colorScheme.primary
				)
			)
		},
		bottomBar = {
			Button(
				onClick = { showDialog = true },
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
					.navigationBarsPadding(),
				shape = RoundedCornerShape(16.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = colorScheme.primary,
					contentColor = colorScheme.onPrimary
				),
				enabled = gymCode != null
			) {
				Icon(Icons.Default.Add, contentDescription = null)
				Spacer(modifier = Modifier.width(8.dp))
				Text("Generar código para cliente")
			}
		},
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(horizontal = 16.dp, vertical = 24.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.Top
		) {
			Text(
				text = "Último código generado:",
				style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
			)
			
			Spacer(modifier = Modifier.height(16.dp))
			
			generatedCode?.let { code ->
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							clipboardManager.setText(AnnotatedString(code))
							Toast.makeText(context, "Código copiado al portapapeles", Toast.LENGTH_SHORT).show()
						},
					shape = RoundedCornerShape(16.dp),
					elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
					colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
				) {
					Row(
						modifier = Modifier
							.padding(16.dp)
							.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Column {
							Text("Código generado:", fontWeight = FontWeight.Medium)
							Spacer(modifier = Modifier.height(6.dp))
							Text(
								text = code,
								style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
								color = colorScheme.primary
							)
							Spacer(modifier = Modifier.height(4.dp))
							Text(
								"Toca la tarjeta para copiar al portapapeles",
								style = MaterialTheme.typography.bodySmall,
								color = colorScheme.onSurfaceVariant
							)
						}
						Icon(
							imageVector = Icons.Default.ContentCopy,
							contentDescription = "Copiar",
							tint = colorScheme.primary
						)
					}
				}
			}
		}
		
		// Diálogo de confirmación
		if (showDialog && gymCode != null) {
			AlertDialog(
				onDismissRequest = { showDialog = false },
				title = { Text("Confirmar generación") },
				text = { Text("¿Deseas generar un nuevo código de acceso para clientes?") },
				confirmButton = {
					TextButton(
						onClick = {
							showDialog = false
							viewModel.SetSelectedRoleForCode("cliente")
							viewModel.generateCodeForRole()
						}
					) {
						Text("Aceptar")
					}
				},
				dismissButton = {
					TextButton(onClick = { showDialog = false }) {
						Text("Cancelar")
					}
				}, containerColor = colorScheme.surface
			)
		}
	}
}