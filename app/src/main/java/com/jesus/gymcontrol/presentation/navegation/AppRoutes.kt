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
	data class EditPersonScreen(val idPerson: Int? = null)

	@Serializable
	data object ListPersonScreen

	@Serializable
	data object PaymentsScreen

	@Serializable
	data object PersonasScreen

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
}