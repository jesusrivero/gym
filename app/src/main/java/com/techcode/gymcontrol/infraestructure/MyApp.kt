package com.techcode.gymcontrol.infraestructure

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application(){
	companion object {
		lateinit var myApp: MyApp
	}
	
	override fun onCreate() {
		super.onCreate()
		myApp = this
	}
}