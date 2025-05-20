package com.techcode.gymcontrol.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.techcode.gymcontrol.data.db.dao.UsuariosDatabaseDao
import com.techcode.gymcontrol.data.db.entity.PersonEntity

@Database(
	entities = [PersonEntity::class],
	version = 1,
//	autoMigrations= [
//		AutoMigration(from = 1, to = 2, ),
//		AutoMigration(from = 2, to = 3, )
//	]
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun usuariosDao(): UsuariosDatabaseDao
}