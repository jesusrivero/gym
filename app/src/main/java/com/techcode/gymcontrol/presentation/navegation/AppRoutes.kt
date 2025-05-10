package com.techcode.gymcontrol.presentation.navegation

import kotlinx.serialization.Serializable

object AppRoutes {
	@Serializable
	data object MainScreen

	@Serializable
	data object RegPersonScreen

	@Serializable
	data object ManageScreen

	
	@Serializable
	data class EditPersonScreen(val idPerson: Int? = null)

	@Serializable
	data object ListPersonScreen
	
	@Serializable
	data object PaymentsScreen
	
	@Serializable
	data object PersonasScreen
}