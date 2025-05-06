package com.techcode.gymcontrol.presentation.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuarios(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,
	@ColumnInfo(name = "usuario")
	val usuario: String,
	@ColumnInfo(name = "email")
	val email: String
)
