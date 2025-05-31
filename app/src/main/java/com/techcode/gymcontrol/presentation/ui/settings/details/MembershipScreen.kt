package com.techcode.gymcontrol.presentation.ui.settings.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.presentation.ui.people.PaymentSettingsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(navController: NavController) {
    val viewModel: PaymentSettingsViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.getPricesValue()

    }
    MembershipContent(
        navController = navController,
        navBottom = navController,
        state = viewModel.paymentState.collectAsState().value,
        onSubmit = { p1, p2, p3, p4, p5, p6 ->
            viewModel.updatePricesMembership(
                WeeklyValue = p1,
                BiweeklyValue = p2,
                MonthlyValue = p3,
                QuarterlyValue = p4,
                BiannualValue = p5,
                AnnualValue = p6

            )
        }
    )
}

fun allFieldsAreValid(
    weekly: String,
    biweekly: String,
    monthly: String,
    quarterly: String,
    biannual: String,
    annual: String
): Boolean {
    return weekly.isNotBlank() &&
            biweekly.isNotBlank() &&
            monthly.isNotBlank() &&
            quarterly.isNotBlank() &&
            biannual.isNotBlank() &&
            annual.isNotBlank()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipContent(
    navController: NavController,
    navBottom: NavController,
    onSubmit: (String, String, String, String, String, String) -> Unit,
    state: PaymentSettingsViewModel.PaymentState,
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var editedWeekly by rememberSaveable { mutableStateOf("") }
    var editedBiweekly by rememberSaveable { mutableStateOf("") }
    var editedMonthly by rememberSaveable { mutableStateOf("") }
    var editedQuarterly by rememberSaveable { mutableStateOf("") }
    var editedBinnual by rememberSaveable { mutableStateOf("") }
    var editedAnnual by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state) {
        state.PricesMembership?.let {
            editedWeekly = it.weekly.orEmpty()
            editedBiweekly = it.biweekly.orEmpty()
            editedMonthly = it.monthly.orEmpty()
            editedQuarterly = it.quarterly.orEmpty()
            editedBinnual = it.biannual.orEmpty()
            editedAnnual = it.annual.orEmpty()
        }

    }

    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(2000) // 3 segundos
            showSnackbar = false
        }
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Listado de membresías",
                            color = Color.White,
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xBAA7D3DC))
                )
            }
        },
        snackbarHost = {
            if (showSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {}
                ) {
                    Text(text = snackbarMessage)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())

        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {

                Box(modifier = Modifier.background(color = Color.White)) {

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Editar valores de pago en dólares",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

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
                                value = editedWeekly.toString(),
                                onValueChange = { editedWeekly = it },
                                label = { Text("Valor semanal") },
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
                                value = editedBiweekly.toString(),
                                onValueChange = { editedBiweekly = it },
                                label = { Text("Valor quincenal") },
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
                                value = editedMonthly.toString(),
                                onValueChange = { editedMonthly = it },
                                label = { Text("Valor mensual") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        // Trimestral
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Trimestral:",
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = editedQuarterly.toString(),
                                onValueChange = { editedQuarterly = it },
                                label = { Text("Valor trimestral") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        // Semestral
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Semestral:",
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = editedBinnual.toString(),
                                onValueChange = { editedBinnual = it },
                                label = { Text("Valor semestral") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        // Anual
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Anual:",
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = editedAnnual.toString(),
                                onValueChange = { editedAnnual = it },
                                label = { Text("Valor semanal") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (allFieldsAreValid(
                                        editedWeekly,
                                        editedBiweekly,
                                        editedMonthly,
                                        editedQuarterly,
                                        editedBinnual,
                                        editedAnnual
                                    )
                                ) {
                                    onSubmit(
                                        editedWeekly,
                                        editedBiweekly,
                                        editedMonthly,
                                        editedQuarterly,
                                        editedBinnual,
                                        editedAnnual
                                    )
                                    snackbarMessage = "Cambios guardados correctamente"
                                    showSnackbar = true
                                } else {
                                    snackbarMessage = "Por favor complete todos los campos"
                                    showSnackbar = true
                                }
                            },
                            modifier = Modifier
                                .padding()
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xBAA7D3DC),
                                disabledContainerColor = Color.LightGray
                            ),
                            enabled = allFieldsAreValid(
                                editedWeekly,
                                editedBiweekly,
                                editedMonthly,
                                editedQuarterly,
                                editedBinnual,
                                editedAnnual
                            )

                        ) {
                            Text("Guardar cambios")
                        }
                    }

                }

            }
        }
    }
}


