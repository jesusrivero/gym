package com.jesus.gymcontrol.infraestructure

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jesus.gymcontrol.R
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager

class MyFirebaseMessagingService : FirebaseMessagingService() {
	
	override fun onNewToken(token: String) {
		super.onNewToken(token)
		
		Log.d("FCM", "Nuevo token FCM: $token")
		
		val uid = FirebaseAuth.getInstance().currentUser?.uid
		
		if (uid != null) {
			val firestore = FirebaseFirestore.getInstance()
			
			val data = mapOf(
				"fcmToken" to token,
				"timestamp" to FieldValue.serverTimestamp()
			)
			
			firestore.collection("users")
				.document(uid)
				.set(data, SetOptions.merge())
				.addOnSuccessListener {
					Log.d("FCM", "Token FCM guardado en Firestore para UID: $uid")
				}
				.addOnFailureListener { e ->
					Log.e("FCM", "Error al guardar token FCM en Firestore: ${e.message}")
				}
		} else {
			Log.w("FCM", "Usuario no autenticado, no se pudo guardar token FCM")
			}
	}
	
	@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		super.onMessageReceived(remoteMessage)
		
		// 🔷 Inicializa PreferencesManager
		val preferencesManager = PreferencesManager(applicationContext)
		val settings = preferencesManager.getNotificationSettings()
		
		if (!settings.pushNotificationsEnabled) {
			Log.d("FCM", "Notificaciones push desactivadas, no se muestra nada.")
			return
		}
		
		val type = remoteMessage.data["type"] ?: "" // 🔷 campo opcional para cuando backend lo envíe
		
		val mostrar = when (type) {
			"new_client" -> settings.newClientEnabled
			"payment_registered" -> settings.promotionExpirationEnabled
			"membership_expiration" -> settings.membershipExpirationEnabled
			"week_start" -> settings.weekStartEnabled
			"" -> true // 🔷 si no viene type aún, muestra para no romper UX
			else -> true // o false, si prefieres bloquear tipos desconocidos
		}
		
		if (!mostrar) {
			Log.d("FCM", "Tipo de notificación $type desactivado por usuario.")
			return
		}
		
		val title = remoteMessage.notification?.title ?: "Notificación"
		val body = remoteMessage.notification?.body ?: ""
		
		showNotification(title, body)
	}
	
	@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
	private fun showNotification(title: String, body: String) {
		val channelId = "estado_cambios"
		
		val notificationManager = NotificationManagerCompat.from(this)
		
		// 🔷 crea canal si necesario
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				"Cambios de Estado",
				NotificationManager.IMPORTANCE_HIGH
			)
			notificationManager.createNotificationChannel(channel)
		}
		
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.drawable.ic_notification) // cambia por tu ícono
			.setContentTitle(title)
			.setContentText(body)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setAutoCancel(true)
		
		notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
	}
}