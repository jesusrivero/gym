package com.jesus.gymcontrol.domain.repository

import com.jesus.gymcontrol.domain.model.Person

interface UsuarioRepository {
	suspend fun agregarUsuario(usuario: Person)
	suspend fun updateUser(usuario: Person)
	suspend fun deleteUser(usuario: Person)
	suspend fun obtenerUsuarios(): List<Person>

	


}