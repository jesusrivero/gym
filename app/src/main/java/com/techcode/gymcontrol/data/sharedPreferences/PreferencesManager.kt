package com.techcode.gymcontrol.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences


class PreferencesManager(context: Context) {
	companion object {
		const val KEY_WEEKLY_VALUE = "weeklyValue"
		const val KEY_BIWEEKLY_VALUE = "biweeklyValue"
		const val KEY_MONTHLY_VALUE = "monthlyValue"
		const val KEY_QUARTERLY_VALUE = "quarterlyValue"
		const val KEY_BINNUAL_VALUE = "binnualValue"
		const val KEY_ANNUAL_VALUE = "annualValue"
	}
	
	
	private val SharedPreferences: SharedPreferences =
		context.getSharedPreferences("GymControlPreferences", Context.MODE_PRIVATE)
	
	fun saveData(Key: String, value: Int){
		val editor = SharedPreferences.edit()
		editor.putInt(Key, value)
		editor.apply()
		
	}
	
	
	fun getData(Key: String): Int {
		return SharedPreferences.getInt(Key, 0)
	}
}

