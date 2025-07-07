//package com.jesus.gymcontrol.domain.repository.sqlLight
//
//
//
//import android.content.Context
//import com.jesus.gymcontrol.GymDatabase
//import com.squareup.sqldelight.android.AndroidSqliteDriver
//import com.squareup.sqldelight.db.SqlDriver
//
//object DatabaseModule {
//	private var driver: SqlDriver? = null
//	private var database: GymDatabase? = null
//
//	fun getDatabase(context: Context): GymDatabase {
//		if (database == null) {
//			driver = AndroidSqliteDriver(
//				schema = GymDatabase.Schema,
//				context = context,
//				name = "gym.db"
//			)
//			database = GymDatabase(driver!!)
//		}
//		return database!!
//		}
//}