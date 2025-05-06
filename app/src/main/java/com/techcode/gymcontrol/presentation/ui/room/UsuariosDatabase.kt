package com.techcode.gymcontrol.presentation.ui.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techcode.gymcontrol.presentation.ui.models.Usuarios

@Database(
	entities = [Usuarios::class],
	version = 1,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun usuariosDao(): UsuariosDatabaseDao
}