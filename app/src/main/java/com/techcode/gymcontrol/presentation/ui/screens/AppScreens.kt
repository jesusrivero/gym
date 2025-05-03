package com.techcode.gymcontrol.presentation.ui.screens

sealed class AppScreens (val route: String){
	object HomeScreen: AppScreens("home_screen")
	object RegistrosScreen: AppScreens("registros_screen")
	object ClientesScreen: AppScreens("clientes_screen")
}