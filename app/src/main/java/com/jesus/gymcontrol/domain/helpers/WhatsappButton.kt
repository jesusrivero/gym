package com.jesus.gymcontrol.domain.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun WhatsAppButton(
	phoneNumber: String,
	message: String,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current
	
	IconButton(
		onClick = {
			openWhatsApp(context, phoneNumber, message)
		},
		modifier = modifier
	) {
		androidx.compose.material3.Icon(
			imageVector = Icons.Default.Message,
			contentDescription = "Enviar por WhatsApp",
			tint = Color(0xFF25D366) // verde WhatsApp
			)
		}
}