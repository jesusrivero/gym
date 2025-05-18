package com.techcode.gymcontrol.presentation.navegation

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

}