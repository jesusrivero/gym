package com.jesus.gymcontrol.domain.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
	val formattedPhone = phoneNumber.replace(" ", "").replace("+", "")
	val encodedMessage = Uri.encode(message)
	
	val uri = Uri.parse("https://wa.me/$formattedPhone?text=$encodedMessage")
	val intent = Intent(Intent.ACTION_VIEW, uri)
	
	try {
		context.startActivity(intent)
	} catch (e: Exception) {
		Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
		}
}