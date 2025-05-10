package com.techcode.gymcontrol.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.ui.main.MainScreen
import com.techcode.gymcontrol.presentation.ui.people.EditPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.ListPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
import com.techcode.gymcontrol.presentation.ui.people.RegPersonScreen
import com.techcode.gymcontrol.presentation.ui.screens.ManageScreen
import com.techcode.gymcontrol.presentation.ui.screens.PaymentsScreen
import com.techcode.gymcontrol.presentation.ui.screens.PersonsScreen

@Composable
fun NavigationHost(
	viewModel: PeopleViewModel,
) {
	val navController = rememberNavController()
	
	NavHost(navController = navController, startDestination = AppRoutes.MainScreen) {
		composable<AppRoutes.MainScreen> {
			MainScreen(
				navController = navController,
				
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
		
		composable<AppRoutes.ManageScreen> {
			ManageScreen(
				navBottom = navController,
				
			)
		}
		
		
		composable<AppRoutes.ListPersonScreen> {
			ListPersonScreen(
				nav = navController
			)
		}
		composable<AppRoutes.PersonasScreen> {
			PersonsScreen(
				viewModel = viewModel,
				navEdit = { navController.navigate(AppRoutes.EditPersonScreen(it)) },
				navBottom = navController)
		}
		
		
		composable<AppRoutes.PaymentsScreen> {
		PaymentsScreen(
				navBottom = navController)
		}
		
		
	}
}

