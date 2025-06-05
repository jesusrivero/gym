package com.techcode.gymcontrol.domain.model

import com.techcode.gymcontrol.data.db.entity.PersonEntity

data class Person(
	val id: Int? = null,
	val usuario: String,
	val email: String,
	val cedula: String,
	val numeroTelefono: String,
	
){
	fun toPersonEntity(): PersonEntity {
		return PersonEntity(
			id = id ?: 0,
			usuario = usuario,
			email = email,
			cedula = cedula,
			numeroTelefono = numeroTelefono)
	}
}
