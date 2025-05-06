package com.techcode.gymcontrol.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.db.entity.PersonEntity

@Database(
	entities = [PersonEntity::class],
	version = 1,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun usuariosDao(): UsuariosDatabaseDao
}