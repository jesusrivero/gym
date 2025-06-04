package com.jesus.gymcontrol.data.repository

// 
import com.jesus.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.jesus.gymcontrol.domain.model.Person
import com.jesus.gymcontrol.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map



class UsuarioRepositoryIMPL (
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
		return dao.obtenerUsuarios().map { list -> list.map{ it.toPerson()} }.first()
	}
}

