package com.jesus.gymcontrol.presentation.ui.people


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.viewmodels.RegisterUserFromAdminViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegPersonScreen(
    navController: NavController,
    viewModel:RegisterUserFromAdminViewModel = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    val isRegistering = viewModel.isRegistering
    val registerSuccess = viewModel.registerSuccess
    val errorMessage = viewModel.errorMessage

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            delay(2000)
            showSnackbar = false
        }
    }

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            snackbarMessage = "Cliente registrado correctamente"
            showSnackbar = true
            viewModel.resetRegisterState()
            navController.navigate(AppRoutes.MainScreen) {
                popUpTo(AppRoutes.RegPersonScreen) { inclusive = true }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarMessage = it
            showSnackbar = true
            viewModel.resetRegisterState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Gym Control",
                        color = colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_back),
                            contentDescription = "Regresar",
                            tint = colorScheme.onPrimary
                        )
                    }
                }
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
    ) { paddingValues ->
        RegPersonContent(
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel,
            isLoading = isRegistering
        )
    }
}

@Composable
fun RegPersonContent(
    modifier: Modifier = Modifier,
    viewModel: RegisterUserFromAdminViewModel,
    isLoading: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var idCard by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var gimnasioCode by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("cliente") }

    var showDialog by remember { mutableStateOf(false) }
    var rolExpanded by remember { mutableStateOf(false) }
    val roles = listOf("cliente", "admin")

    val isEmailValid = email.matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
    val formIsValid = name.isNotBlank() && email.isNotBlank() && password.length >= 6 &&
            code.isNotBlank() && rol.isNotBlank() && isEmailValid

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        viewModel.registerUserAsAdmin(
                            email = email,
                            password = password,
                            name = name,
                            phone = phone,
                            idCard = idCard,
                            gender = "",
                            age = 0,
                            membership = "",
                            code = code,
                            gimnasioCode = gimnasioCode,
                            rol = rol
                        )
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar registro") },
            text = { Text("¿Deseas registrar a esta persona con rol '$rol'?") }
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Gmail") },
            isError = email.isNotBlank() && !isEmailValid,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña (mín. 6 caracteres)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = idCard,
            onValueChange = { idCard = it },
            label = { Text("Cédula") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Código") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rol,
            onValueChange = {},
            label = { Text("Rol") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = colorScheme.outline,
                disabledTextColor = colorScheme.onSurface,
                disabledLabelColor = colorScheme.onSurfaceVariant
            ),
            enabled = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            enabled = formIsValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading)
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            else
                Text("Registrar", style = MaterialTheme.typography.labelLarge)
        }
    }
}