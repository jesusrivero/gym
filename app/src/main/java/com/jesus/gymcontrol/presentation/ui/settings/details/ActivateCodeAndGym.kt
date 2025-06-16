package com.jesus.gymcontrol.presentation.ui.settings.details

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jesus.gymcontrol.domain.model.Gym
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.domain.viewmodels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivateCodeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    gymViewModel: GymViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    GymTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ActivateCodeContent(
                navController = navController,
                authViewModel = authViewModel,
                gymViewModel = gymViewModel,
                userViewModel = userViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivateCodeContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    gymViewModel: GymViewModel,
    userViewModel: UserViewModel
) {
    val colorScheme = MaterialTheme.colorScheme
    var code by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authError = authViewModel.errorMessage
    val gymError = gymViewModel.errorMessage
    val userError = userViewModel.errorMessage
    val isLoading = gymViewModel.isLoading || userViewModel.isLoading
    val isGymCreated = gymViewModel.isSuccess
    val isCodeValid = gymViewModel.isCodeValid
    val codeValidationError = gymViewModel.codeValidationError
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(authError, gymError, userError) {
        authError?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        gymError?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        userError?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    LaunchedEffect(isGymCreated) {
        if (isGymCreated && currentUser != null) {
            val gym = Gym(
                ownerId = currentUser.uid,
                ownername = authViewModel.name,
                code = gymViewModel.code,
                name = gymViewModel.name,
                direction = gymViewModel.direction,
                phone = gymViewModel.phone,
                admin = "",
                coach = ""
            )

            userViewModel.assignGymToUser(
                uid = currentUser.uid,
                gym = gym,
                rol = "Dueño",
	            navController = navController,
                onSuccess = {
                    // 👉 Guardamos el rol del usuario localmente
                    authViewModel.newDatesUserLogin("Dueño", code, navController)

                    // Marcamos el código como usado
                    gymViewModel.markCodeAsUsed(code, rol)

                    // Reseteamos validación y navegamos
                    gymViewModel.resetValidation()

                    navController.navigate(AppRoutes.MainScreen) {
                        popUpTo(AppRoutes.StartScreen) { inclusive = true }
                    }
                },
                onError = {}
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Activar Gimnasio",
                        color = colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_back),
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Código de activación") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (code.isNotBlank()) {
                        gymViewModel.validateOwnerCode(code.trim())
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                enabled = code.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Validar código", color = Color.White)
            }

            codeValidationError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            if (isCodeValid == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ Código válido", color = Color.Green, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    "Datos del gimnasio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = gymViewModel.name,
                    onValueChange = { gymViewModel.name = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nombre del gimnasio") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = com.jesus.gymcontrol.R.drawable.ic_payments),
                            contentDescription = null
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = gymViewModel.direction,
                    onValueChange = { gymViewModel.direction = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Dirección") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = gymViewModel.phone,
                    onValueChange = { gymViewModel.phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Teléfono") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.newDatesUserLogin("Dueño", code, navController)
                        gymViewModel.code = code
                        gymViewModel.createGym()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Guardar Gimnasio", color = Color.White)
                }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun ActivateCodePreview() {
//    GymTheme {
//        ActivateCodeContent(navController = rememberNavController())
//    }
//}