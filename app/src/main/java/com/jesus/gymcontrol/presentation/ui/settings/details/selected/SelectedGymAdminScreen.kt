package com.jesus.gymcontrol.presentation.ui.settings.details.selected

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.R
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
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = { Text("Buscar gimnasio...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        // Acción al seleccionar gimnasio
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_launcher),
                                            contentDescription = null,
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = gym.nombre,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("📍 Dirección: ${gym.direccion}", style = MaterialTheme.typography.bodyMedium)
                                    Text("📞 Teléfono: ${gym.telefono}", style = MaterialTheme.typography.bodyMedium)
                                    Text("🔑 Código: ${gym.codigo}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}