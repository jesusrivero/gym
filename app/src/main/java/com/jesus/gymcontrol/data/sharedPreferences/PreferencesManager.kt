package com.jesus.gymcontrol.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.jesus.gymcontrol.domain.model.NotificationSettings
import com.jesus.gymcontrol.domain.model.PricesMembership
import com.jesus.gymcontrol.extensions.fromJson
import com.jesus.gymcontrol.extensions.getJson

class PreferencesManager(context: Context) {
	companion object {
		const val PRICES_MEMBERSHIP = "PricesMembership"
		// Precios de membresías
//		const val KEY_WEEKLY_VALUE = "weeklyValue"
//		const val KEY_BIWEEKLY_VALUE = "biweeklyValue"
//		const val KEY_MONTHLY_VALUE = "monthlyValue"
//		const val KEY_QUARTERLY_VALUE = "quarterlyValue"
//		const val KEY_BINNUAL_VALUE = "binnualValue"
//		const val KEY_ANNUAL_VALUE = "annualValue"


		private const val NOTIFICATION_PREFS = "NotificationPrefs"
		const val KEY_NEW_CLIENT = "new_client"
		const val KEY_PAYMENT_REGISTERED = "payment_registered"
		const val KEY_MEMBERSHIP_EXPIRATION = "membership_expiration"
		const val KEY_WEEK_START = "week_start"
		const val KEY_PUSH_NOTIFICATIONS = "push_notifications"
	}

	private val sharedPreferences: SharedPreferences =
		context.getSharedPreferences("GymControlPreferences", Context.MODE_PRIVATE)
	private val notificationPrefs: SharedPreferences =
		context.getSharedPreferences(NOTIFICATION_PREFS, Context.MODE_PRIVATE)

	val editor = sharedPreferences.edit()
	private val notificationEditor = notificationPrefs.edit()

//	// Métodos para precios de membresías
//	fun saveData(Key: String, value: Int) {
//		editor.putInt(Key, value)
//		editor.apply()
//	}
//
//	fun getData(Key: String): Int {
//		return sharedPreferences.getInt(Key, 0)
//	}

	fun savePrices(value: PricesMembership) {
		editor.putString(PRICES_MEMBERSHIP, value.getJson()).apply()
	}

	fun getPrices(): PricesMembership? {
		val json = sharedPreferences.getString(PRICES_MEMBERSHIP, null)
		return json?.fromJson()
	}
	
	fun saveNotificationSettings(
		newClient: Boolean,
		paymentRegistered: Boolean,
		membershipExpiration: Boolean,
		weekStart: Boolean,
		pushNotifications: Boolean
	) {
		with(notificationEditor) {
			putBoolean(KEY_NEW_CLIENT, newClient)
			putBoolean(KEY_PAYMENT_REGISTERED, paymentRegistered)
			putBoolean(KEY_MEMBERSHIP_EXPIRATION, membershipExpiration)
			putBoolean(KEY_WEEK_START, weekStart)
			putBoolean(KEY_PUSH_NOTIFICATIONS, pushNotifications)
			apply()
		}
	}

	fun getNotificationSettings(): NotificationSettings {
		return NotificationSettings(
			newClientEnabled = notificationPrefs.getBoolean(KEY_NEW_CLIENT, true),
			paymentRegisteredEnabled = notificationPrefs.getBoolean(KEY_PAYMENT_REGISTERED, true),
			membershipExpirationEnabled = notificationPrefs.getBoolean(KEY_MEMBERSHIP_EXPIRATION, true),
			weekStartEnabled = notificationPrefs.getBoolean(KEY_WEEK_START, true),
			pushNotificationsEnabled = notificationPrefs.getBoolean(KEY_PUSH_NOTIFICATIONS, true)
		)
	}
}





