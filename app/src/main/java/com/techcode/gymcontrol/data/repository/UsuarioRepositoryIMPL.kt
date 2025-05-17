package com.techcode.gymcontrol.data.repository

import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.domain.model.Person
import com.techcode.gymcontrol.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

class UsuarioRepositoryIMPL(
	private val dao: UsuariosDatabaseDao
): UsuarioRepository  {
	override suspend fun agregarUsuario(usuario: Person) {
		dao.agregarUsuario(usuario = usuario.toPersonEntity())
	}
	override suspend fun updateUser(usuario: Person) {
		dao.actualizarUsuario(usuario = usuario.toPersonEntity())
	}
	
	override suspend fun deleteUser(usuario: Person) {
		dao.borrarUsuario(usuario = usuario.toPersonEntity())
	}
	
	override suspend fun obtenerUsuarios(): List<Person> {
		var ListPerson: List<Person> = emptyList()
		dao.obtenerUsuarios().collectLatest { res ->
			ListPerson = res.map { it.toPerson()}

		}
		return ListPerson
	}
}

//actualizarUsuario