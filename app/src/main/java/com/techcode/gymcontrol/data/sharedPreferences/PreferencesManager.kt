package com.techcode.gymcontrol.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences


class PreferencesManager(context: Context) {
	private val SharedPreferences: SharedPreferences =
		context.getSharedPreferences("GymControlPreferences", Context.MODE_PRIVATE)
	
	fun saveData(Key: String, value: String){
		val editor = SharedPreferences.edit()
		editor.putString(Key, value)
		editor.apply()
		
	}
	
	
	fun getData(Key: String): String? {
		return SharedPreferences.getString(Key, null)
	}
}

