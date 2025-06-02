package com.techcode.gymcontrol.presentation.ui.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.theme.GymTheme


@Composable
fun LoginScreen(navController: NavController) {
    GymTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoginContent(navController)
        }
    }
}

@Composable
fun LoginContent(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme
    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hola,",
            color = colorScheme.onBackground,
            fontSize = 24.sp
        )
        Text(
            text = "Bienvenido de vuelta",
            color = colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = emailOrUser,
            onValueChange = { emailOrUser = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
            placeholder = {
                Text(
                    text = "Correo o usuario",
                    color = colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono correo",
                    tint = colorScheme.onSurfaceVariant
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
            placeholder = {
                Text(
                    text = "Contraseña",
                    color = colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono candado",
                    tint = colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                val visibilityIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = "Toggle Visibilidad",
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = colorScheme.outline,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(AppRoutes.MainScreen) },
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Ingresar", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = colorScheme.outline
            )
            Text(
                text = "  o  ",
                color = colorScheme.outline,
                fontSize = 14.sp
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(56.dp)
                .background(colorScheme.surface, shape = CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_google),
                contentDescription = "Google Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = colorScheme.onBackground,
                fontSize = 14.sp
            )
            Text(
                text = "Regístrate",
                color = colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(AppRoutes.RegisterScreen)
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginContentPreview() {
//    LoginContent()
//}