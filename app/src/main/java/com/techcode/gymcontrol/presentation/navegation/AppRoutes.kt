package com.techcode.gymcontrol.presentation.navegation

import kotlinx.serialization.Serializable

object AppRoutes {
	@Serializable
	data object MainScreen

	@Serializable
	data object RegPersonScreen

	
	
	@Serializable
	data class EditPersonScreen(val idPerson: Int? = null)

	@Serializable
	data object ListPersonScreen
	
	@Serializable
	data object PersonasScreen
}