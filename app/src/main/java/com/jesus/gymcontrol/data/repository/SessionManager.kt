package com.jesus.gymcontrol.data.repository
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private fun getSessionKey(key: String): String {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return key
        return "${uid}_$key"
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        val key = getSessionKey("is_logged_in")
        sharedPreferences.edit { putBoolean(key, isLoggedIn) }
    }

    fun saveRoleState(roleAssigned: Boolean) {
        val key = getSessionKey("is_role_Assigned")
        sharedPreferences.edit { putBoolean(key, roleAssigned) }
    }



    fun clearSession() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        sharedPreferences.edit {
            remove("${uid}_is_logged_in")
            remove("${uid}_is_role_Assigned")
        }
    }

    fun isLoggedIn(): Boolean {
        val key = getSessionKey("is_logged_in")
        return sharedPreferences.getBoolean(key, false  )
    }

    fun isRoleAssigned(): Boolean {
        val key = getSessionKey("is_role_Assigned")
        return sharedPreferences.getBoolean(key, false  )
    }
}