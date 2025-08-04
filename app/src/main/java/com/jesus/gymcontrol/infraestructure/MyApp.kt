package com.jesus.gymcontrol.infraestructure

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
	companion object {
		lateinit var myApp: MyApp
	}
	
	override fun onCreate() {
		super.onCreate()
		myApp = this
		
		// Inicializar ThreeTenABP para compatibilidad de fechas
		AndroidThreeTen.init(this)
	}
}
