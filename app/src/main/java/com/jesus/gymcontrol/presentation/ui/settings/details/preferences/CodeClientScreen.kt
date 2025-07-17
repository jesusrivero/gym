package com.jesus.gymcontrol.presentation.ui.settings.details.preferences


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.generateQrBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


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
	viewModel: GymViewModel = hiltViewModel(),
) {
	val colorScheme = MaterialTheme.colorScheme
	val clipboardManager = LocalClipboardManager.current
	val context = LocalContext.current
	
	var showDialog by remember { mutableStateOf(false) }
	
	val generatedCode = viewModel.generatedCode
	val errorMessage = viewModel.errorMessage
	val snackbarHostState = remember { SnackbarHostState() }
	
	val gymCode = viewModel.gymCode
	val userUid = FirebaseAuth.getInstance().currentUser?.uid
	
	val availableCodes by viewModel.availableCodes.collectAsState()
	val codesError by viewModel.codesError.collectAsState()
	val currentUserRole = viewModel.currentUserRole
	
	LaunchedEffect(Unit) {
		userUid?.let {
			viewModel.loadCurrentUserGymCode(it)
			viewModel.loadCurrentUserRole(it)
		}
		viewModel.loadAvailableCodes()
	}
	
	LaunchedEffect(errorMessage) {
		errorMessage?.let {
			snackbarHostState.showSnackbar(it)
			viewModel.clearErrorMessage()
		}
	}
	
	// Filtrar códigos según rol
	val filteredCodes = remember(availableCodes, currentUserRole) {
		if (currentUserRole == "dueño") {
			availableCodes
		} else {
			availableCodes.filter { it.rol == "cliente" }
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
							painterResource(id = R.drawable.ic_back),
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
				Text("Generar código")
			}
		},
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { innerPadding ->
		
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.Top
		) {
			generatedCode?.let { code ->
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp),
					shape = RoundedCornerShape(16.dp),
					elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
					colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
				) {
					Column(
						modifier = Modifier
							.padding(16.dp)
							.fillMaxWidth(),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text("Código generado:", fontWeight = FontWeight.Medium)
						Spacer(modifier = Modifier.height(6.dp))
						Text(
							text = code,
							style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
							color = colorScheme.primary
						)
						Spacer(modifier = Modifier.height(8.dp))
						Text(
							"Toca para copiar al portapapeles",
							style = MaterialTheme.typography.bodySmall,
							color = colorScheme.onSurfaceVariant
						)
						Spacer(modifier = Modifier.height(12.dp))
						
						GeneratedQrWithShare(
							code = code,
							onShare = { bitmap ->
								shareQrCode(context, bitmap)
							}
						)
					}
				}
			}
			
			Spacer(modifier = Modifier.height(24.dp))
			
			Text(
				text = "Códigos disponibles:",
				style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			if (filteredCodes.isEmpty()) {
				Text(
					text = "No hay códigos disponibles actualmente.",
					style = MaterialTheme.typography.bodyMedium,
					color = colorScheme.onSurfaceVariant
				)
			} else {
				LazyColumn {
					items(filteredCodes) { codeInfo ->
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 6.dp)
								.clickable {
									clipboardManager.setText(AnnotatedString(codeInfo.code))
									Toast.makeText(context, "Código copiado: ${codeInfo.code}", Toast.LENGTH_SHORT)
										.show()
								},
							shape = RoundedCornerShape(12.dp),
							elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
							colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
						) {
							Row(
								modifier = Modifier
									.padding(16.dp)
									.fillMaxWidth(),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Column {
									Text(
										text = codeInfo.code,
										style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
									)
									Spacer(modifier = Modifier.height(4.dp))
									Text(
										text = "Rol: ${codeInfo.rol.replaceFirstChar { it.uppercase() }}",
										style = MaterialTheme.typography.bodySmall,
										color = colorScheme.primary
									)
								}
								Icon(
									imageVector = Icons.Default.ContentCopy,
									contentDescription = "Copiar código",
									tint = colorScheme.primary
								)
							}
						}
					}
				}
			}
		}
	}
	
	if (showDialog && gymCode != null) {
		AlertDialog(
			onDismissRequest = { showDialog = false },
			title = { Text("Confirmar generación") },
			text = {
				if (currentUserRole == "dueño") {
					Column {
						Text("Selecciona el rol para el nuevo código:")
						Spacer(modifier = Modifier.height(8.dp))
						Row {
							listOf("cliente", "administrador").forEach { role ->
								Row(verticalAlignment = Alignment.CenterVertically) {
									RadioButton(
										selected = viewModel.setSelectedRoleForCode == role,
										onClick = { viewModel.SetSelectedRoleForCode(role) }
									)
									Text(role.replaceFirstChar { it.uppercase() })
									Spacer(Modifier.width(8.dp))
								}
							}
						}
					}
				} else {
					Text("¿Deseas generar un nuevo código de acceso para cliente?")
					viewModel.SetSelectedRoleForCode("cliente")
				}
			},
			confirmButton = {
				TextButton(
					onClick = {
						showDialog = false
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
			},
			containerColor = colorScheme.surface
		)
	}
}



@Composable
fun GeneratedQr(code: String) {
	val bitmap = remember(code) {
		generateQrBitmap(code)
	}
	
	Image(
		bitmap = bitmap.asImageBitmap(),
		contentDescription = "Código QR",
		modifier = Modifier
			.size(200.dp)
			.clip(RoundedCornerShape(8.dp))
	)
}


fun shareQrCode(context: Context, bitmap: Bitmap) {
	val cachePath = File(context.cacheDir, "qr_images")
	cachePath.mkdirs()
	val file = File(cachePath, "qr_code.png")
	FileOutputStream(file).use { out ->
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
	}
	
	val qrUri = FileProvider.getUriForFile(
		context,
		"${context.packageName}.fileprovider",
		file
	)
	
	val shareIntent = Intent(Intent.ACTION_SEND).apply {
		type = "image/png"
		putExtra(Intent.EXTRA_STREAM, qrUri)
		addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	}
	
	context.startActivity(
		Intent.createChooser(shareIntent, "Compartir código QR")
	)
}


@Composable
fun GeneratedQrWithShare(
	code: String,
	onShare: (Bitmap) -> Unit,
) {
	val context = LocalContext.current
	var bitmap by remember { mutableStateOf<Bitmap?>(null) }
	
	LaunchedEffect(code) {
		withContext(Dispatchers.Default) {
			bitmap = generateQrBitmap(code)
		}
	}
	
	if (bitmap != null) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Image(
				bitmap = bitmap!!.asImageBitmap(),
				contentDescription = "Código QR",
				modifier = Modifier
					.size(200.dp)
					.clip(RoundedCornerShape(8.dp))
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Button(onClick = { onShare(bitmap!!) }) {
				Icon(Icons.Default.Share, contentDescription = null)
				Spacer(Modifier.width(8.dp))
				Text("Compartir QR")
			}
		}
	} else {
		CircularProgressIndicator()
	}
}


