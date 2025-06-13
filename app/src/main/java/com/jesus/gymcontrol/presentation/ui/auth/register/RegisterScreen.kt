package com.jesus.gymcontrol.presentation.ui.auth.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import androidx.compose.animation.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    GymTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RegisterContent(navController)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterContent(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel: AuthViewModel = hiltViewModel()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var idcard by remember { mutableStateOf("") }
    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }
    var step by remember { mutableStateOf(0) }

    val isValidEmail = emailOrUser.trim().matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
    val isValidCedula = idcard.all { it.isDigit() } && idcard.length in 6..8
    val isFormValid = name.isNotBlank() && lastName.isNotBlank() && isValidCedula
    val isFullFormValid = isFormValid && isValidEmail && password.length >= 6 && termsAccepted

    val transition = updateTransition(targetState = step, label = "formStep")
    val scroll = rememberScrollState()


    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            navController.navigate(AppRoutes.LoginScreen)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("Hola,", fontSize = 24.sp, color = colorScheme.onBackground)
        Text("Registremos tu cuenta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colorScheme.onBackground)

        Spacer(modifier = Modifier.height(24.dp))

        transition.AnimatedContent(
            transitionSpec = { fadeIn() with fadeOut() },
        ) { targetStep ->
            if (targetStep == 0) {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        placeholder = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        placeholder = { Text("Apellido") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = idcard,
                        onValueChange = { idcard = it },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        placeholder = { Text("Cédula") },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                        isError = idcard.isNotBlank() && !isValidCedula,
                        singleLine = true
                    )
                    if (idcard.isNotBlank() && !isValidCedula) {
                        Text("Cédula inválida", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { step = 1 },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("Siguiente")
                    }
                }
            } else {
                Column {
                    OutlinedTextField(
                        value = emailOrUser,
                        onValueChange = { emailOrUser = it },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        placeholder = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        isError = emailOrUser.isNotBlank() && !isValidEmail,
                        singleLine = true
                    )
                    if (emailOrUser.isNotBlank() && !isValidEmail) {
                        Text("Debe ser un correo válido de Gmail", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        placeholder = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
                        Text("Acepto las políticas de privacidad", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.registerUser(emailOrUser, password, "$name $lastName", idcard)
                        },
                        enabled = isFullFormValid && !viewModel.isLoading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = colorScheme.onPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Registrar", color = colorScheme.onPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = { step = 0 }) {
                        Text("Atrás", color = colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Ya tienes una cuenta?", fontSize = 14.sp)
            Text(
                " Inicia sesión",
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.navigate(AppRoutes.LoginScreen) }
            )
        }

        if (viewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(viewModel.errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

