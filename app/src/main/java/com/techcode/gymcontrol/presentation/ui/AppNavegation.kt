package com.techcode.gymcontrol.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techcode.gymcontrol.presentation.ui.screens.AgregarScreen
import com.techcode.gymcontrol.presentation.ui.screens.AppScreens
import com.techcode.gymcontrol.presentation.ui.screens.EditarScreen
import com.techcode.gymcontrol.presentation.ui.screens.MainScreen
import com.techcode.gymcontrol.presentation.ui.screens.RegistrosScreen
import com.techcode.gymcontrol.presentation.ui.viewmodels.UsuariosViewModel

sealed class HomeScreen(val route: String) {
	object Home : HomeScreen("home_screen")
	object Registros : HomeScreen("registros_screen")
}

@Composable
fun AppNavegation(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	viewModel: UsuariosViewModel
) {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route)
	{
		composable(AppScreens.HomeScreen.route) {
			MainScreen(
				it = PaddingValues(),
				navController = navController,
				viewModel = viewModel
			)
		}
		composable(route = "agregar") {
			AgregarScreen(navController, viewModel)
		}
		composable(
			route = "editar/{id}/{usuario}/{email}", arguments = listOf(
			navArgument("id") { type = NavType.IntType },
			navArgument("usuario") { type = NavType.StringType },
			navArgument("email") { type = NavType.StringType }
		)) {
			EditarScreen(
				navController,
				viewModel,
				it.arguments!!.getInt("id"),
				it.arguments?.getString("usuario"),
				it.arguments?.getString("email")
			)
		}
		composable(AppScreens.RegistrosScreen.route) {
			RegistrosScreen(navController)
		}
		
	}
}

