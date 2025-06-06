package com.jesus.gymcontrol.presentation.ui.auth


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.domain.models.AuthViewModel
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme

@Composable
fun LoginScreen(navController: NavController) {
	val viewModel: AuthViewModel = hiltViewModel()
	GymTheme {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			LoginContent(navController = navController, viewModel = viewModel)
		}
	}
}

@Composable
fun LoginContent(
	navController: NavController,
	viewModel: AuthViewModel,
) {
	val loginState by viewModel.loginState.collectAsState()
	val context = LocalContext.current
	val colorScheme = MaterialTheme.colorScheme
	var passwordVisible by remember { mutableStateOf(false) }
	
	
	
	LaunchedEffect(loginState) {
		loginState?.let { result ->
			if (result.isSuccess) {
				navController.navigate(AppRoutes.MainScreen) {
					popUpTo(AppRoutes.LoginScreen) { inclusive = true }
				}
			} else {
				val error = result.exceptionOrNull()?.message ?: "Error desconocido"
				Toast.makeText(context, error, Toast.LENGTH_LONG).show()
			}
			viewModel.clearStateLogin()
		}
	}
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Spacer(modifier = Modifier.height(24.dp))
		
		Text("Hola,", color = colorScheme.onBackground, fontSize = 24.sp)
		Text(
			"Bienvenido de vuelta",
			color = colorScheme.onBackground,
			fontSize = 28.sp,
			fontWeight = FontWeight.Bold
		)
		
		Spacer(modifier = Modifier.height(32.dp))
		
		OutlinedTextField(
			value = viewModel.email.value,
			onValueChange = { viewModel.email.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
			placeholder = { Text("Correo o usuario", color = colorScheme.onSurfaceVariant) },
			leadingIcon = {
				Icon(
					imageVector = Icons.Default.Email,
					contentDescription = null,
					tint = colorScheme.onSurfaceVariant
				)
			},
			singleLine = true
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		OutlinedTextField(
			value = viewModel.password.value,
			onValueChange = { viewModel.password.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			textStyle = LocalTextStyle.current.copy(color = colorScheme.onSurface),
			placeholder = { Text("Contraseña", color = colorScheme.onSurfaceVariant) },
			leadingIcon = {
				Icon(
					imageVector = Icons.Default.Lock,
					contentDescription = null,
					tint = colorScheme.onSurfaceVariant
				)
			},
			trailingIcon = {
				Icon(
					imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
			"¿Olvidaste tu contraseña?",
			color = colorScheme.outline,
			fontSize = 14.sp,
			modifier = Modifier
				.align(Alignment.End)
				.clickable {
					navController.navigate(AppRoutes.RecoverPasswordScreen)
				}
		)
		
		Spacer(modifier = Modifier.height(32.dp))
		
		Button(
			onClick = {
				if (viewModel.email.value.isNotBlank() && viewModel.password.value.isNotBlank()) {
					viewModel.login(viewModel.email.value.trim(), viewModel.password.value.trim())
				} else {
					Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
				}
			},
			colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			shape = RoundedCornerShape(28.dp),
		) {
			Text("Ingresar", fontSize = 16.sp)
		}
		
		Spacer(modifier = Modifier.height(24.dp))
		
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			Divider(modifier = Modifier.weight(1f), color = colorScheme.outline)
			Text("  o  ", color = colorScheme.outline, fontSize = 14.sp)
			Divider(modifier = Modifier.weight(1f), color = colorScheme.outline)
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
				painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_google),
				contentDescription = "Google Icon",
				modifier = Modifier.size(32.dp)
			)
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Row {
			Text("¿No tienes una cuenta? ", color = colorScheme.onBackground, fontSize = 14.sp)
			Text(
				"Regístrate",
				color = colorScheme.primary,
				fontSize = 14.sp,
				fontWeight = FontWeight.Bold,
				modifier = Modifier.clickable {
					navController.navigate(AppRoutes.RegisterScreen)
				}
			)
		}
		
		Spacer(modifier = Modifier.height(24.dp))
	}
}

//@Preview(showBackground = true)
//@Composable
//fun LoginContentPreview() {
//    LoginContent()
//}