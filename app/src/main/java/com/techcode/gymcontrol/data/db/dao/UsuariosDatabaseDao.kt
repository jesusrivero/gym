package com.techcode.gymcontrol.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.techcode.gymcontrol.data.db.entity.PersonEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UsuariosDatabaseDao {
	@Query("SELECT * FROM usuarios")
	fun obtenerUsuarios(): Flow<List<PersonEntity>>
	@Query("SELECT * FROM usuarios WHERE id = :id")
	fun obtenerUsuario(id: Int): Flow<PersonEntity>

	@Insert
	suspend fun agregarUsuario(usuario: PersonEntity)

	@Update
	suspend fun actualizarUsuario(usuario: PersonEntity)

	@Delete
	suspend fun borrarUsuario(usuario: PersonEntity)


}