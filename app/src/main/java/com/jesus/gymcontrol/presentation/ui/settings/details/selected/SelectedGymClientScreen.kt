package com.jesus.gymcontrol.presentation.ui.settings.details.selected

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedGymClient(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: GymViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val colorScheme = colorScheme

    val searchQuery = viewModel.searchQuery
    val gyms = viewModel.filteredGyms()
    val isLoading = viewModel.isLoading
    val context = LocalContext.current

    var showCodeField by remember { mutableStateOf(false) }
    var codeInput by remember { mutableStateOf("") }
    var selectedGym by remember { mutableStateOf<Gym?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllGyms()
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
                    LazyColumn {
                        items(gyms) { gym ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        selectedGym = gym
                                        showCodeField = true
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(gym.nombre, fontWeight = FontWeight.Bold)
                                    Text("Código: ${gym.codigo}")
                                    Text("Dirección: ${gym.direccion}")
                                    Text("Teléfono: ${gym.telefono}")
                                }
                            }
                        }
                    }
                }

                if (showCodeField && selectedGym != null) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Ingresa el código del gimnasio seleccionado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = { codeInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Código de validación") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.VpnKey, contentDescription = null)
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val gym = selectedGym!!
                            if (codeInput.trim() == gym.codigo) {
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                currentUser?.let { user ->
                                    authViewModel.newDatesUserLogin("Cliente", gym.codigo)
                                    userViewModel.assignGymToUser(
                                        uid = user.uid,
                                        gym = gym,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Gimnasio asignado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate(AppRoutes.MainScreen) {
                                                popUpTo("SelectedGymClient") { inclusive = true }
                                            }
                                        },
                                        onError = { error ->
                                            Toast.makeText(
                                                context,
                                                "Error al asignar: $error",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Código incorrecto", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            }
        }
    }
}