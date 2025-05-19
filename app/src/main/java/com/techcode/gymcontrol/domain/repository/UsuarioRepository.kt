package com.techcode.gymcontrol.domain.repository

import com.techcode.gymcontrol.domain.model.Person

interface UsuarioRepository {
	suspend fun agregarUsuario(usuario: Person)

}