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
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivateCodeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    gymViewModel: GymViewModel = hiltViewModel()
) {
    GymTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ActivateCodeContent(
                navController = navController,
                authViewModel = authViewModel,
                gymViewModel = gymViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivateCodeContent(
    navController: NavController,
    authViewModel: AuthViewModel,
    gymViewModel: GymViewModel
) {
    val colorScheme = MaterialTheme.colorScheme
    var code by remember { mutableStateOf("") }
    var showFields by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authError = authViewModel.errorMessage
    val gymError = gymViewModel.errorMessage
    val isLoading = gymViewModel.isLoading
    val isSuccess = gymViewModel.isSuccess


    LaunchedEffect(authError, gymError) {
        authError?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
        gymError?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }


    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.navigate(AppRoutes.MainScreen) {
                popUpTo(AppRoutes.StartScreen) { inclusive = true }
            }
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
                onClick = { showFields = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Activar", color = Color.White)
            }

            if (showFields) {
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
                        if (code.isNotBlank()) {
                            authViewModel.newDatesUserLogin("Dueño", code)
                            gymViewModel.code = code
                            gymViewModel.createGym()
                        } else {
                            Toast.makeText(context, "Código requerido", Toast.LENGTH_SHORT).show()
                        }
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