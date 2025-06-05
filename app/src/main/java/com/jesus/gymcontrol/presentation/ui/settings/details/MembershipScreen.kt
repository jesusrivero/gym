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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.R
import com.techcode.gymcontrol.presentation.theme.GymTheme
import com.techcode.gymcontrol.presentation.ui.people.PaymentSettingsViewModel
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(navController: NavController) {
    val viewModel: PaymentSettingsViewModel = hiltViewModel()
    val state by viewModel.paymentState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPricesValue()
    }

    var showUpdateSnackbar by remember { mutableStateOf(false) }
    LaunchedEffect(state.PricesMembership) {
        if (state.PricesMembership != null) {
            showUpdateSnackbar = true
        }
    }

    GymTheme {
        MembershipContent(
            navController = navController,
            navBottom = navController,
            state = state,
            onSubmit = { p1, p2, p3, p4, p5, p6 ->
                viewModel.updatePricesMembership(
                    WeeklyValue = p1,
                    BiweeklyValue = p2,
                    MonthlyValue = p3,
                    QuarterlyValue = p4,
                    BiannualValue = p5,
                    AnnualValue = p6
                )
            },
            showUpdateSnackbar = showUpdateSnackbar,
            onDismissSnackbar = { showUpdateSnackbar = false }
        )
    }
}

fun allFieldsAreValid(
    weekly: String,
    biweekly: String,
    monthly: String,
    quarterly: String,
    biannual: String,
    annual: String
): Boolean {
    return weekly.isNotBlank() && weekly.toDoubleOrNull() != null &&
            biweekly.isNotBlank() && biweekly.toDoubleOrNull() != null &&
            monthly.isNotBlank() && monthly.toDoubleOrNull() != null &&
            quarterly.isNotBlank() && quarterly.toDoubleOrNull() != null &&
            biannual.isNotBlank() && biannual.toDoubleOrNull() != null &&
            annual.isNotBlank() && annual.toDoubleOrNull() != null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipContent(
    navController: NavController,
    navBottom: NavController,
    onSubmit: (String, String, String, String, String, String) -> Unit,
    state: PaymentSettingsViewModel.PaymentState,
    showUpdateSnackbar: Boolean = false,
    onDismissSnackbar: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    var editedWeekly by rememberSaveable { mutableStateOf("") }
    var editedBiweekly by rememberSaveable { mutableStateOf("") }
    var editedMonthly by rememberSaveable { mutableStateOf("") }
    var editedQuarterly by rememberSaveable { mutableStateOf("") }
    var editedBiannual by rememberSaveable { mutableStateOf("") }
    var editedAnnual by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.PricesMembership) {
        state.PricesMembership?.let {
            editedWeekly = it.weekly.orEmpty()
            editedBiweekly = it.biweekly.orEmpty()
            editedMonthly = it.monthly.orEmpty()
            editedQuarterly = it.quarterly.orEmpty()
            editedBiannual = it.biannual.orEmpty()
            editedAnnual = it.annual.orEmpty()
        }
    }

    if (showSnackbar) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSnackbar = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Listado de membresías",
                        color = colorScheme.onPrimary,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navBottom.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Regresar",
                            tint = colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primary)
            )
        },
        snackbarHost = {
            if (showSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = snackbarMessage)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.background)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Editar valores de pago en dólares",
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MembershipTextField("Semanal", editedWeekly) { editedWeekly = it }
                    MembershipTextField("Quincenal", editedBiweekly) { editedBiweekly = it }
                    MembershipTextField("Mensual", editedMonthly) { editedMonthly = it }
                    MembershipTextField("Trimestral", editedQuarterly) { editedQuarterly = it }
                    MembershipTextField("Semestral", editedBiannual) { editedBiannual = it }
                    MembershipTextField("Anual", editedAnnual) { editedAnnual = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (allFieldsAreValid(
                                    editedWeekly,
                                    editedBiweekly,
                                    editedMonthly,
                                    editedQuarterly,
                                    editedBiannual,
                                    editedAnnual
                                )
                            ) {
                                onSubmit(
                                    editedWeekly,
                                    editedBiweekly,
                                    editedMonthly,
                                    editedQuarterly,
                                    editedBiannual,
                                    editedAnnual
                                )
                                snackbarMessage = "Cambios guardados correctamente"
                            } else {
                                snackbarMessage = "Por favor complete todos los campos"
                            }
                            showSnackbar = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = allFieldsAreValid(
                            editedWeekly,
                            editedBiweekly,
                            editedMonthly,
                            editedQuarterly,
                            editedBiannual,
                            editedAnnual
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                    ) {
                        Text("Guardar cambios")
                    }
                }
            }
        }
    }
}

@Composable
fun MembershipTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    val typography = MaterialTheme.typography
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "$label:", style = typography.bodyMedium)
        OutlinedTextField(
            value = value,
	        maxLines = 1,
            onValueChange = {
                if (it.isEmpty() || it.toDoubleOrNull() != null) onValueChange(it)
            },
            label = { Text("Valor $label".lowercase()) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
}
