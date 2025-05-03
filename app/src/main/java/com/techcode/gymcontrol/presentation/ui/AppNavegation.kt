package com.techcode.gymcontrol.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.ui.componentes.BottomNavItem
import com.techcode.gymcontrol.presentation.ui.screens.AppScreens
import com.techcode.gymcontrol.presentation.ui.screens.MainScreen
import com.techcode.gymcontrol.presentation.ui.screens.RegistrosScreen

sealed class HomeScreen(val route: String) {
	object Home : HomeScreen("home_screen")
	object Registros : HomeScreen("registros_screen")
}

@Composable
fun AppNavegation(navController: NavHostController, modifier: Modifier = Modifier) {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route)
	{
		composable(AppScreens.HomeScreen.route) {
			MainScreen(navController)
		}
		composable(AppScreens.RegistrosScreen.route) {
			RegistrosScreen(navController)
		}
		
	}
}

