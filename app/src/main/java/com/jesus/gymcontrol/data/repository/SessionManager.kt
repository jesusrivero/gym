package com.jesus.gymcontrol.data.repository
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
	
	private val sharedPreferences: SharedPreferences =
		context.getSharedPreferences("gym_prefs", Context.MODE_PRIVATE)
	
	private fun getSessionKey(key: String): String {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return key
		return "${uid}_$key"
	}
	
	fun saveLoginState(isLoggedIn: Boolean) {
		val key = getSessionKey("is_logged_in")
		sharedPreferences.edit { putBoolean(key, isLoggedIn) }
	}
	
	fun isLoggedIn(): Boolean {
		val key = getSessionKey("is_logged_in")
		return sharedPreferences.getBoolean(key, false)
	}
	
	fun saveRoleState(roleAssigned: Boolean) {
		val key = getSessionKey("is_role_assigned")
		sharedPreferences.edit { putBoolean(key, roleAssigned) }
	}
	
	fun isRoleAssigned(): Boolean {
		val key = getSessionKey("is_role_assigned")
		return sharedPreferences.getBoolean(key, false)
	}
	
	fun clearSession() {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
		sharedPreferences.edit {
			remove("${uid}_is_logged_in")
			remove("${uid}_is_role_assigned")
			remove("${uid}_role")
			remove("${uid}_gym_code")
			remove("${uid}_gymId")            // 🔷 limpia gymId
			remove("${uid}_override_id")
		}
	}
	
	fun setUserSessionData(uid: String, rol: String, gymCode: String, overrideId: String? = null) {
		sharedPreferences.edit().apply {
			putString("${uid}_role", rol)
			putString("${uid}_gym_code", gymCode)
			putBoolean("${uid}_is_role_assigned", true)
			overrideId?.let {
				putString("${uid}_override_id", it)
			}
		}.apply()
	}
	
	fun getRol(): String? {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
		return sharedPreferences.getString("${uid}_role", null)
	}
	
	fun getGymCode(): String? {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
		return sharedPreferences.getString("${uid}_gym_code", null)
	}
	

	fun getGymId(): String? {
		val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
		return sharedPreferences.getString("${uid}_gymId", null)
	}
	
	fun getUserUid(): String? {
		return FirebaseAuth.getInstance().currentUser?.uid
		}
}


