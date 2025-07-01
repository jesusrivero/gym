package com.jesus.gymcontrol.infraestructure

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jesus.gymcontrol.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
	
	override fun onNewToken(token: String) {
		super.onNewToken(token)
		Log.d("FCM", "Nuevo token FCM: $token")
		
		// Aquí puedes subir el token a Firestore con el UID del usuario
		// para que puedas enviarle notificaciones desde el backend
	}
	
	@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		super.onMessageReceived(remoteMessage)
		val title = remoteMessage.notification?.title ?: "Notificación"
		val body = remoteMessage.notification?.body ?: ""
		
		showNotification(title, body)
	}
	
	@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
	private fun showNotification(title: String, body: String) {
		val channelId = "estado_cambios"
		
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.drawable.ic_notification) // cambia por tu ícono
			.setContentTitle(title)
			.setContentText(body)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setAutoCancel(true)
		
		val notificationManager = NotificationManagerCompat.from(this)
		notificationManager.notify(0, notificationBuilder.build())

		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId,
				"Cambios de Estado",
				NotificationManager.IMPORTANCE_HIGH
			)
			notificationManager.createNotificationChannel(channel)
		}
		
		notificationManager.notify(0, notificationBuilder.build())
	}
}