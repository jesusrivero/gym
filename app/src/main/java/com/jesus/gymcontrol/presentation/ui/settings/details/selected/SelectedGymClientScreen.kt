package com.jesus.gymcontrol.presentation.ui.settings.details.selected

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedGymClient(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: GymViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    gymViewModel: GymViewModel = hiltViewModel(),
    sessionManager: SessionManager,
) {
    val colorScheme = MaterialTheme.colorScheme
    val searchQuery = viewModel.searchQuery
    val gyms = viewModel.filteredGyms()
    val isLoading = viewModel.isLoading
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }
    var selectedGym by remember { mutableStateOf<Gym?>(null) }
    var isValidatingCode by remember { mutableStateOf(false) }

    val isCodeValid = viewModel.isCodeValid
    val codeValidationError = viewModel.codeValidationError
    val rol by remember { mutableStateOf("cliente") }

    LaunchedEffect(Unit) {
        viewModel.fetchAllGyms()
    }

    LaunchedEffect(isCodeValid) {
        if (isValidatingCode && isCodeValid != null) {
            isValidatingCode = false

            if (isCodeValid) {
                selectedGym?.let { gym ->
                    // Asignar el gimnasio al usuario
                    userViewModel.onConfirmAssignGym(
                        gym = gym,
                        context = context,
                        rol = "cliente",
                        navController = navController
                    )

                    // Actualizar la sesión y navegar desde el ViewModel
                    authViewModel.newDatesUserLogin(
                        rol = "cliente",
                        code = code,
                        navController = navController
                    )

                    // Marcar código como usado
                    viewModel.markCodeAsUsed(code = code, rol = "cliente")

                    // Resetear estado
                    gymViewModel.resetValidation()
                    code = ""
                    showDialog = false
                }
            }
        }
    }
    GymTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Selecciona un gimnasio",
                            color = colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Regresar",
                                tint = colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar gimnasio") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    gyms.forEach { gym ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    sessionManager.saveRoleState(!rol.isNullOrBlank())
                                    selectedGym = gym
                                    viewModel.resetValidation()
                                    showDialog = true
                                }
                                .border(
                                    width = 1.dp,
                                    color = colorScheme.outline.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(gym.name, fontWeight = FontWeight.Bold)
                                    Text("Código: ${gym.code}")
                                    Text("Dirección: ${gym.direction}")
                                    Text("Teléfono: ${gym.phone}")
                                }

                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = colorScheme.primary.copy(alpha = 0.1f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "Ver código QR",
                                        tint = colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }


            if (showDialog && selectedGym != null) {
                AlertDialog(
                    onDismissRequest = {
                        if (!isValidatingCode) {
                            showDialog = false
                            code = ""
                            viewModel.resetValidation()
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isValidatingCode = true
                                viewModel.validateClientCode(
                                    code = code.trim(),
                                    gymCode = selectedGym!!.code.trim(),

                                    )
                            },
                            enabled = code.isNotBlank() && !isValidatingCode
                        ) {
                            if (isValidatingCode) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(end = 8.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                            Text("Validar", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                if (!isValidatingCode) {
                                    showDialog = false
                                    code = ""
                                    viewModel.resetValidation()
                                }
                            }
                        ) {
                            Text("Cancelar")
                        }
                    },
                    title = {
                        Text("Código de validación", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Column {
                            Text("Ingresa el código de validación para unirte a ${selectedGym!!.name}")
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = code,
                                onValueChange = { code = it },
                                placeholder = { Text("Código") },
                                leadingIcon = {
                                    Icon(Icons.Default.VpnKey, contentDescription = null)
                                },
                                singleLine = true,
                                isError = codeValidationError != null,
                                enabled = !isValidatingCode,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (codeValidationError != null) {
                                Text(
                                    text = codeValidationError,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    },
                    containerColor = colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}