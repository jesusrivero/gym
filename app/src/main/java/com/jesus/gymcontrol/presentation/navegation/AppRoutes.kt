package com.jesus.gymcontrol.presentation.navegation

import kotlinx.serialization.Serializable

object AppRoutes {
	@Serializable
	data object MainScreen
	
	@Serializable
	data object RegPersonScreen
	
	@Serializable
	data object PreferencesScreen
	
	
	@Serializable
	data object TutorialScreen
	
	@Serializable
	data object InactiveScreen
	
	@Serializable
	data object PromotionsScreen
	
	// ✅ Cambiado aquí
	@Serializable
	data object PaymentsScreen {
		const val baseRoute = "payments_screen/{uid}/{nombre}"
		
		fun route(uid: String, nombre: String): String {
			return "payments_screen/$uid/$nombre"
		}
	}
	
	@Serializable
	data object PersonasScreen {
		const val route = "PersonasScreen"
	}
	
	
	@Serializable
	data object ManageScreen
	
	@Serializable
	data object ContactScreen
	
	@Serializable
	data object ListPaymentsScreen
	
	@Serializable
	data object ReportScreen
	
	@Serializable
	data object MembershipScreen
	
	@Serializable
	data object ErrorReportScreen
	
	@Serializable
	data object NotificationScreen
	
	@Serializable
	data object SecurityScreen
	
	@Serializable
	data object AccountScreen
	
	@Serializable
	data object LoginScreen
	
	@Serializable
	data object RegisterScreen
	
	@Serializable
	data object StartScreen
	
	@Serializable
	data object RecoverPasswordScreen
	
	@Serializable
	data object SelectedRolScreen
	
	@Serializable
	data object ActivateCodeScreen
	
	@Serializable
	data object SelectedGymAdmin
	
	@Serializable
	data object SelectedGymClient
	
	@Serializable
	data object CodeClientScreen
	
	@Serializable
	data object OwnerMainScreen
	
	@Serializable
	data object ClientMainScreen
	
	@Serializable
	data object NotificationsScreen
	
	@Serializable
	data object PerworkersScreen
}
