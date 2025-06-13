package com.jesus.gymcontrol.presentation.ui.settings.details.selected

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.presentation.theme.GymTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedGymAdmin(
    navController: NavController,
    viewModel: GymViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val searchQuery = viewModel.searchQuery
    val gyms = viewModel.filteredGyms()
    val isLoading = viewModel.isLoading
    val codeValidationError = viewModel.codeValidationError
    val isCodeValid = viewModel.isCodeValid

    var showDialog by remember { mutableStateOf(false) }
    var selectedGym by remember { mutableStateOf<Gym?>(null) }
    var codeInput by remember { mutableStateOf("") }
    var isValidatingCode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllGyms()
    }

    LaunchedEffect(isCodeValid) {
        if (isValidatingCode && isCodeValid != null) {
            isValidatingCode = false
            if (isCodeValid) {
                showDialog = false
                codeInput = ""
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
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)

            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar gimnasio...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(gyms) { gym ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
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
            }
        }

        if (showDialog && selectedGym != null) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    codeInput = ""
                    viewModel.resetValidation()
                },
                title = {
                    Text("Código de validación", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text("IIngresa el código de validación para unirte a ${selectedGym!!.name}")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = codeInput,
                            onValueChange = { codeInput = it },
                            placeholder = { Text("Código") },
                            leadingIcon = {
                                Icon(Icons.Default.VpnKey, contentDescription = null)
                            },
                            singleLine = true,
                            isError = codeValidationError != null,
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
                confirmButton = {
                    TextButton(
                        onClick = {
                            isValidatingCode = true
                            viewModel.validateClientCode(
                                code = codeInput.trim(),
                                gymCode = selectedGym!!.code.trim()
                            )
                        },
                        enabled = codeInput.isNotBlank() && !isValidatingCode
                    ) {
                        if (isValidatingCode) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Validar", fontWeight = FontWeight.Bold)
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            codeInput = ""
                            viewModel.resetValidation()
                        }
                    ) {
                        Text("Cancelar")
                    }
                },
                containerColor = colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
