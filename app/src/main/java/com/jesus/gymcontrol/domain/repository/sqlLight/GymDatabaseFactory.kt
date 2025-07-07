package com.jesus.gymcontrol.domain.repository.sqlLight


//
//import android.content.Context
//import app.cash.sqldelight.driver.android.AndroidSqliteDriver
//import com.squareup.sqldelight.db.SqlDriver
//import com.jesus.gymcontrol.data.db.GymDatabase
//
//object GymDatabaseFactory {
//	@Volatile
//	private var instance: GymDatabase? = null
//
//	fun getDatabase(context: Context): GymDatabase {
//		return instance ?: synchronized(this) {
//			val driver: SqlDriver = AndroidSqliteDriver(
//				schema = GymDatabase.Schema,
//				context = context,
//				name = "gym.db"
//			)
//			val database = GymDatabase(driver)
//			instance = database
//			database
//			}
//		}
//}