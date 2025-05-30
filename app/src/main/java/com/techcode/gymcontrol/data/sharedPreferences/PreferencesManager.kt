package com.techcode.gymcontrol.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.techcode.gymcontrol.domain.model.PricesMembership
import com.techcode.gymcontrol.extensions.fromJson
import com.techcode.gymcontrol.extensions.getJson


class PreferencesManager(context: Context) {
	companion object {
		const val KEY_WEEKLY_VALUE = "weeklyValue"
		const val KEY_BIWEEKLY_VALUE = "biweeklyValue"
		const val KEY_MONTHLY_VALUE = "monthlyValue"
		const val KEY_QUARTERLY_VALUE = "quarterlyValue"
		const val KEY_BINNUAL_VALUE = "binnualValue"
		const val KEY_ANNUAL_VALUE = "annualValue"
		const val PRICES_MEMBERSHIP = "PricesMembership"
	}
	
	
	private val SharedPreferences: SharedPreferences =
		context.getSharedPreferences("GymControlPreferences", Context.MODE_PRIVATE)
	val editor = SharedPreferences.edit()
	fun saveData(Key: String, value: Int) {
		editor.putInt(Key, value)
		editor.apply()
		
	}
	
	fun getData(Key: String): Int {
		return SharedPreferences.getInt(Key, 0)
	}
	
	
	fun savePrices(value: PricesMembership) {
		editor.putString(PRICES_MEMBERSHIP, value.getJson()).apply()
	}
	
	fun getPrices():PricesMembership?{
		val json = SharedPreferences.getString(PRICES_MEMBERSHIP, null)
		return json?.fromJson()
	}
	
	
	
	
}



