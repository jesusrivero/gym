package com.techcode.gymcontrol.presentation.navegation

import ReportScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.ui.main.MainScreen
import com.techcode.gymcontrol.presentation.ui.people.EditPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.ListPersonScreen
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
import com.techcode.gymcontrol.presentation.ui.people.RegPersonScreen
import com.techcode.gymcontrol.presentation.ui.screens.SubScreen.ContactScreen
import com.techcode.gymcontrol.presentation.ui.screens.SubScreen.ListPaymentsScreen
import com.techcode.gymcontrol.presentation.ui.screens.ManageScreen
import com.techcode.gymcontrol.presentation.ui.screens.PreferencesScreen
import com.techcode.gymcontrol.presentation.ui.screens.PaymentsScreen
import com.techcode.gymcontrol.presentation.ui.screens.SubScreen.ErrorReportScreen
import com.techcode.gymcontrol.presentation.ui.screens.SubScreen.PersonsScreen
import com.techcode.gymcontrol.presentation.ui.screens.SubScreen.MembershipScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost (
	viewModel: PeopleViewModel= hiltViewModel()
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
		
		composable<AppRoutes.PreferencesScreen> {
			PreferencesScreen(
				navController= navController,

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
		
		composable<AppRoutes.ContactScreen> {
			ContactScreen( navController= navController )
			
		}

		composable<AppRoutes.ErrorReportScreen> {
			ErrorReportScreen( navController= navController )

		}

		composable<AppRoutes.ListPaymentsScreen> {
			ListPaymentsScreen(
				viewModel = viewModel,
				navEdit = { navController.navigate(AppRoutes.EditPersonScreen(it)) },
				navBottom = navController)

		}

		composable<AppRoutes.ManageScreen> {
			ManageScreen(
				navController= navController)
		}

		composable <AppRoutes.MembershipScreen> {
			MembershipScreen(
				navController= navController)

		}

		composable<AppRoutes.ReportScreen> {
			ReportScreen(navController= navController )

		}



	}
}

