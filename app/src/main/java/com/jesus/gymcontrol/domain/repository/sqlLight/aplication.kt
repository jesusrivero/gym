//package com.jesus.gymcontrol
//
//import android.app.Application
//import com.jesus.gymcontrol.domain.repository.sqlLight.GymDatabaseFactory
//
//class GymApp : Application() {
//	lateinit var gymDatabase: GymDatabase
//		private set
//
//	override fun onCreate() {
//		super.onCreate()
//		gymDatabase = GymDatabaseFactory.getDatabase(this)
//	}
//}