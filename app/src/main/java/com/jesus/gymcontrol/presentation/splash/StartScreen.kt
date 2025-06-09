package com.jesus.gymcontrol.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(500)
        viewModel.loadSessionState()
        delay(500)

        val isLoggedIn = viewModel.isLoggedInState
        val hasRole = viewModel.isRoleAssignedState

        when {
            isLoggedIn && hasRole -> {
                navController.navigate(AppRoutes.MainScreen) {
                    popUpTo(0) { inclusive = true }
                }
            }

            isLoggedIn && !hasRole -> {
                navController.navigate(AppRoutes.SelectedRolScreen) {
                    popUpTo(0) { inclusive = true }
                }
            }

            else -> {
                navController.navigate(AppRoutes.LoginScreen) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Cargando...")
    }
}