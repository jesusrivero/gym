package com.jesus.gymcontrol.presentation.navegation

import ReportScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesus.gymcontrol.data.repository.SessionManager
import com.jesus.gymcontrol.domain.viewmodels.GymViewModel
import com.jesus.gymcontrol.presentation.splash.StartScreen
import com.jesus.gymcontrol.presentation.ui.auth.LoginScreen
import com.jesus.gymcontrol.presentation.ui.auth.recover.RecoverPasswordScreen
import com.jesus.gymcontrol.presentation.ui.auth.register.RegisterScreen
import com.jesus.gymcontrol.presentation.ui.main.MainScreen
import com.jesus.gymcontrol.presentation.ui.people.EditPersonScreen
import com.jesus.gymcontrol.presentation.ui.people.PeopleViewModel
import com.jesus.gymcontrol.presentation.ui.people.RegPersonScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.ActivateCodeScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.ClientMainScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.OwnerMainScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.ListPaymentsScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.ManageScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.MembershipScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.PersonsScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.manage.PromotionScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.payments.PaymentsScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.AccountScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.CodeClientScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.ContactScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.ErrorReportScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.NotificationScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.NotificationsScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.PreferencesScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.preferences.SecurityScreen
import com.jesus.gymcontrol.presentation.ui.settings.details.selected.SelectedGymAdmin
import com.jesus.gymcontrol.presentation.ui.settings.details.selected.SelectedGymClient
import com.jesus.gymcontrol.presentation.ui.settings.details.selected.SelectedRolScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
	viewModel: PeopleViewModel = hiltViewModel(),
	
	) {
	val context = LocalContext.current
	val navController = rememberNavController()
	val sessionManager = remember { SessionManager(context) }
	val gymViewModel: GymViewModel = hiltViewModel()
	
	
	NavHost(navController = navController, startDestination = AppRoutes.StartScreen) {
		
		composable<AppRoutes.MainScreen> {
			MainScreen(
				navController = navController,
			)
		}
		
		composable<AppRoutes.StartScreen> {
			StartScreen(
				navController = navController
			)
		}
		
		
		composable<AppRoutes.RegPersonScreen> {
			RegPersonScreen(navController)
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
				navController = navController,
				
				)
		}
		
		
		composable<AppRoutes.PersonasScreen> {
			PersonsScreen(
				navEdit = { navController.navigate(AppRoutes.EditPersonScreen()) },
				navPag = { navController.navigate(AppRoutes.PaymentsScreen) },
				navBottom = navController,
				
				
				)
		}
		
		
		composable<AppRoutes.PaymentsScreen> {
			PaymentsScreen(navController = navController)
		}
		
		composable<AppRoutes.ContactScreen> {
			ContactScreen(navController = navController)
			
		}
		
		composable<AppRoutes.ErrorReportScreen> {
			ErrorReportScreen(navController = navController)
			
		}
		
		composable<AppRoutes.ListPaymentsScreen> {
			ListPaymentsScreen(
				navBottom = navController,
				navPagToScreen = { navController.navigate(AppRoutes.PaymentsScreen) }
			)
			
		}
		
		composable<AppRoutes.ManageScreen> {
			ManageScreen(
				navController = navController
			)
		}
		
		composable<AppRoutes.MembershipScreen> {
			MembershipScreen(
				navController = navController,
				
				
				)
			
		}
		
		composable<AppRoutes.PromotionsScreen> {
			PromotionScreen(
				navController = navController,
				
				
				)
			
		}
		
		composable<AppRoutes.ReportScreen> {
			ReportScreen(navController = navController)
			
		}
		composable<AppRoutes.AccountScreen> {
			AccountScreen(navController = navController)
			
		}
		
		composable<AppRoutes.LoginScreen> {
			LoginScreen(navController = navController)
			
		}
		
		composable<AppRoutes.RegisterScreen> {
			RegisterScreen(navController = navController)
			
		}
		
		composable<AppRoutes.RecoverPasswordScreen> {
			RecoverPasswordScreen(navController = navController)
			
		}
		
		
		composable<AppRoutes.SecurityScreen> {
			SecurityScreen(navController = navController)
			
		}
		
		composable<AppRoutes.NotificationScreen> {
			NotificationScreen(navController = navController)
			
		}
		
		composable<AppRoutes.SelectedRolScreen> {
			SelectedRolScreen(navController = navController)
		}
		
		composable<AppRoutes.ActivateCodeScreen> {
			ActivateCodeScreen(navController = navController)
		}
		
		
		
		composable<AppRoutes.SelectedGymAdmin> {
			SelectedGymAdmin(
				navController = navController,
				gymViewModel = gymViewModel,
				sessionManager = sessionManager
			)
		}
		
		composable<AppRoutes.SelectedGymClient> {
			SelectedGymClient(
				navController = navController,
				gymViewModel = gymViewModel,
				sessionManager = sessionManager
			)
		}
		
		composable<AppRoutes.CodeClientScreen> {
			CodeClientScreen(navController = navController)
		}
		
		composable<AppRoutes.ClientMainScreen> {
			ClientMainScreen(navController = navController)
		}
		
		composable<AppRoutes.OwnerMainScreen> {
			OwnerMainScreen(
				navController = navController,
				gymViewModel = gymViewModel,
			)
		}
		
		composable<AppRoutes.NotificationsScreen> {
			NotificationsScreen(navController = navController)
		}
		
	}
}