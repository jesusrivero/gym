package com.jesus.gymcontrol.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jesus.gymcontrol.domain.model.Person

@Entity(tableName = "usuarios")
data class PersonEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	val usuario: String,
	val email: String,
	val cedula: String,
	val numeroTelefono: String,
) {
	fun toPerson(): Person {
		return Person(
			id = id,
			usuario = usuario,
			email = email,
			cedula = cedula,
			numeroTelefono = numeroTelefono
		)
	}
	
}

