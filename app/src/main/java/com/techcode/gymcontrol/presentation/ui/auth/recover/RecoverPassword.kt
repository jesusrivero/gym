package com.techcode.gymcontrol.presentation.ui.auth.recover

import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.theme.GymTheme

@Composable
fun RecoverPasswordScreen(navController: NavController) {
    GymTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            RecoverPasswordContent(navController)
        }
    }
}

@Composable
fun RecoverPasswordContent(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember(email) { mutableStateOf(false) }


    isValidEmail = email.trim().matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recuperar contraseña",
            color = colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingresa tu correo @gmail.com para enviarte un código de recuperación.",
            color = colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
            placeholder = {
                Text("Correo electrónico", color = colorScheme.onSurfaceVariant)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono correo",
                    tint = colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            isError = email.isNotBlank() && !isValidEmail
        )

        if (email.isNotBlank() && !isValidEmail) {
            Text(
                text = "Debe ser un correo válido de Gmail",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Aquí tengo que colocar la logica para el envio del correo de recuperacion
                navController.popBackStack()
            },
            enabled = isValidEmail,
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
        ) {
            Text(text = "Enviar código", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Volver al inicio de sesión",
            color = colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }
}
