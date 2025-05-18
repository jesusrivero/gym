package com.techcode.gymcontrol.presentation.ui.screens.SubScreen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar
import com.techcode.gymcontrol.presentation.ui.people.PaymentSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(navController: NavController) {
    val viewModel: PaymentSettingsViewModel = viewModel()
    MembershipContent(
        navController = navController,
        navBottom = navController,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipContent(
    navController: NavController,
    navBottom: NavController,
    viewModel: PaymentSettingsViewModel
) {
    val paymentValues by viewModel.paymentValues.collectAsState()

    // Estados derivados que reaccionan a cambios en el ViewModel
    val weeklyValue = remember(paymentValues) { paymentValues["Semanal"] ?: "" }
    val biweeklyValue = remember(paymentValues) { paymentValues["Quincenal"] ?: "" }
    val monthlyValue = remember(paymentValues) { paymentValues["Mensual"] ?: "" }

    // Estados editables
    var editedWeekly by remember { mutableStateOf(weeklyValue) }
    var editedBiweekly by remember { mutableStateOf(biweeklyValue) }
    var editedMonthly by remember { mutableStateOf(monthlyValue) }

    // Sincronizar cuando cambian los valores del ViewModel
    LaunchedEffect(paymentValues) {
        editedWeekly = paymentValues["Semanal"] ?: ""
        editedBiweekly = paymentValues["Quincenal"] ?: ""
        editedMonthly = paymentValues["Mensual"] ?: ""
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Listado de pagos", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navBottom.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xBAA7D3DC))
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())

        ) {
            Text(
                text = "Configuración de Valores de Pago",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Editar valores de pago en dólares",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Semanal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Semanal:",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editedWeekly,
                            onValueChange = { editedWeekly = it },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    // Quincenal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Quincenal:",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editedBiweekly,
                            onValueChange = { editedBiweekly = it },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }


                    // Mensual
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Mensual:",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editedMonthly,
                            onValueChange = { editedMonthly = it },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Validar que los valores no estén vacíos
                                if (editedWeekly.isNotBlank() &&
                                    editedBiweekly.isNotBlank() &&
                                    editedMonthly.isNotBlank()) {

                                    val newValues = mapOf(
                                        "Semanal" to editedWeekly,
                                        "Quincenal" to editedBiweekly,
                                        "Mensual" to editedMonthly
                                    )
                                    viewModel.updatePaymentValues(newValues)

                                    // Opcional: Mostrar mensaje de éxito
                                    // scaffoldState.snackbarHostState.showSnackbar("Valores actualizados")
                                } else {
                                    // Mostrar error de validación
                                    // scaffoldState.snackbarHostState.showSnackbar("Complete todos los campos")
                                }
                            },
                            modifier = Modifier.padding(),
                            enabled = editedWeekly.isNotBlank() &&
                                    editedBiweekly.isNotBlank() &&
                                    editedMonthly.isNotBlank()
                        ) {
                            Text("Guardar cambios")
                        }
                }
            }
        }
    }
}}


