package com.techcode.gymcontrol.data.repository

import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository

class UsuarioRepositoryIMPL(
	private val dao: UsuariosDatabaseDao
): UsuarioRepository  {
	override suspend fun agregarUsuario(usuario: Person) {
		dao.agregarUsuario(usuario = usuario.toPersonEntity())
	}
}