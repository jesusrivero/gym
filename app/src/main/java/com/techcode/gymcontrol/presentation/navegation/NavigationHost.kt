package com.techcode.gymcontrol.presentation.navegation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.ui.main.MainScreen
import com.techcode.gymcontrol.presentation.ui.people.EditPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.ListPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.RegPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel

@Composable
fun NavigationHost(
	viewModel: PeopleViewModel
) {
	val navController = rememberNavController()

	NavHost(navController = navController, startDestination = AppRoutes.MainScreen) {
		composable<AppRoutes.MainScreen> {
			MainScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
		composable<AppRoutes.RegPersonScreen> {
			RegPersonScreen(navController, viewModel)
		}
		composable<AppRoutes.EditPersonScreen> {
			val idPerson = it.arguments?.getInt("idPerson")

			EditPersonScreen(
				navController = navController,
				viewModel = viewModel,
				id = idPerson ?: 0
			)
		}
		composable<AppRoutes.ListPersonScreen> {
			ListPersonScreen(
				nav = navController
			)
		}
	}
}

