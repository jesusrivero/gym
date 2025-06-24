package com.jesus.gymcontrol.domain.model

import androidx.compose.foundation.isSystemInDarkTheme

data class ListUser(
	val id: String = "",
	val name: String = "",
	val idcard: String = "",
	val date: Long? = null,
	val phone: String = "",
	val email: String = "",
	val enabled: Boolean = false,
	val state: String = "",
)