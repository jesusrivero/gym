package com.techcode.gymcontrol.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techcode.gymcontrol.domain.model.Person

@Entity(tableName = "usuarios")
data class PersonEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	@ColumnInfo(name = "usuario")
	val usuario: String,
	@ColumnInfo(name = "email")
	val email: String,
	@ColumnInfo(name = "cedula")
	val cedula: String,
	@ColumnInfo(name = "numeroTelefono")
	val numeroTelefono: String
) {
	fun toPerson():Person {
		return Person(
			id = id,
			usuario= usuario,
			email= email,
			cedula= cedula,
			numeroTelefono= numeroTelefono
		)
	}
	
	
}

