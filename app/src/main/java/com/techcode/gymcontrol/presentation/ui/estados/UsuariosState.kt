package com.techcode.gymcontrol.presentation.ui.estados

import com.techcode.gymcontrol.presentation.ui.models.Usuarios

data class UsuariosState(
	val listaUsuarios: List<Usuarios> = emptyList()
)

